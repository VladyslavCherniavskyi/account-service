package com.vlinvestment.accountservice.service.messaging.impl;

import com.vlinvestment.accountservice.entity.AccessCode;
import com.vlinvestment.accountservice.exeption.VerificationCodeException;
import com.vlinvestment.accountservice.service.AccessCodeService;
import com.vlinvestment.accountservice.service.UserService;
import com.vlinvestment.accountservice.service.messaging.MessagingSource;
import com.vlinvestment.accountservice.service.messaging.SenderVerificationCodeService;
import com.vlinvestment.accountservice.service.messaging.TelegramBot;
import com.vlinvestment.accountservice.utils.GeneratorCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Executors;

@Service
@Transactional
@RequiredArgsConstructor
public class SenderVerificationCodeServiceImpl implements SenderVerificationCodeService {

    private final AccessCodeService accessCodeService;
    private final UserService userService;
    private final TelegramBot telegramBot;

    @Override
    public String sendVerificationCode(MessagingSource messagingSource, String phoneOrEmail) {
        switch (messagingSource) {
            case TELEGRAM -> sendVerificationCodeToTelegram(phoneOrEmail);
            case VIBER -> sendVerificationCodeToViber(phoneOrEmail);
            case PHONE -> sendVerificationCodeToSms(phoneOrEmail);
            case EMAIL -> sendVerificationCodeToEmail(phoneOrEmail);
            default -> throw new VerificationCodeException("You have selected an incorrect messagingSource");
        }
        return String.format("Your verification code has been sent to %s", messagingSource);
    }

    private void sendVerificationCodeToTelegram(String phone) {
        var code = GeneratorCodeUtil.generateNumber();
        var executorService = Executors.newFixedThreadPool(3);
        executorService.execute(() -> userService.setPasswordHash(phone, code));
        executorService.execute(() -> accessCodeService.create(
                        AccessCode.builder()
                                .source(phone)
                                .code(code)
                                .build()
                )
        );
        executorService.execute(() -> telegramBot.sendVerificationCode(phone, code));
        executorService.shutdown();
    }

    private void sendVerificationCodeToViber(String source) {
        System.out.println("send to " + source); //TODO create logic for viber provider
    }

    private void sendVerificationCodeToSms(String source) {
        System.out.println("send to " + source); //TODO create logic for sms provider
    }

    private void sendVerificationCodeToEmail(String source) {
        System.out.println("send to " + source); //TODO create logic for email provider
    }

}

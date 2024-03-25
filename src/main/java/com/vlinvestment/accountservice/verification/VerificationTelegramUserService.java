package com.vlinvestment.accountservice.verification;

import com.vlinvestment.accountservice.exeption.VerificationCodeException;
import com.vlinvestment.accountservice.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationTelegramUserService {

    private final TelegramUserService telegramUserService;

    @Value("${telegram.bot.url}")
    private String botUrl;

    public void verifyExistChatId(String phone) {
        if (!telegramUserService.existByPhone(phone)) {
            throw new VerificationCodeException(
                    String.format("You should share your contact in telegram %s", botUrl)
            );
        }

    }

}
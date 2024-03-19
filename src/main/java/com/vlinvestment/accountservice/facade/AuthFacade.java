package com.vlinvestment.accountservice.facade;

import com.vlinvestment.accountservice.dto.request.LoginPhoneDtoRequest;
import com.vlinvestment.accountservice.dto.request.UserDtoCreateRequest;
import com.vlinvestment.accountservice.dto.request.UserLoginDtoRequest;
import com.vlinvestment.accountservice.dto.response.AuthDtoResponse;
import com.vlinvestment.accountservice.mapper.UserMapper;
import com.vlinvestment.accountservice.service.UserService;
import com.vlinvestment.accountservice.service.messaging.SenderVerificationCodeService;
import com.vlinvestment.accountservice.verification.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final UserService userService;
    private final SenderVerificationCodeService senderVerificationCodeService;
    private final UserMapper userMapper;
    private final VerificationService verificationService;

    public AuthDtoResponse login(UserLoginDtoRequest request) {
        var match = Stream.of(
                verificationService.isMatchSource(request.phone()),
                verificationService.isMatchVerificationCode(request.phone(), request.verificationCode())
        ).allMatch(isMatch -> isMatch);
        if (match) {
            return new AuthDtoResponse("User", "This successful");
        }
        return new AuthDtoResponse("User", "This not successful");
    }

    public AuthDtoResponse registerByTelegramBot(UserDtoCreateRequest request) {

        return new AuthDtoResponse(request.phone(), "chatId");
    }

    public String sendVerificationCode(LoginPhoneDtoRequest request) {
        return senderVerificationCodeService.sendVerificationCode(request.messagingSource(), request.phone());
    }
}

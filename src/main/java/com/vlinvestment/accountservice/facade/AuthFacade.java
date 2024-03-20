package com.vlinvestment.accountservice.facade;

import com.vlinvestment.accountservice.dto.request.AuthDtoRequest;
import com.vlinvestment.accountservice.dto.request.UserDtoCreateRequest;
import com.vlinvestment.accountservice.dto.request.UserSignInDtoRequest;
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

    public AuthDtoResponse login(UserSignInDtoRequest request) {
        var match = Stream.of(
                verificationService.isMatchSource(request.phoneOrEmail()),
                verificationService.isMatchVerificationCode(request.phoneOrEmail(), request.verificationCode())
        ).allMatch(isMatch -> isMatch);
        if (match) { //TODO create security
            return new AuthDtoResponse("User", "This successful");
        }
        return new AuthDtoResponse("User", "This not successful");
    }

    public String sendVerificationCode(AuthDtoRequest request) {
        return senderVerificationCodeService.sendVerificationCode(request.messagingSource(), request.phoneOrEmail());
    }

    public String completedPhoneOrEmail(UserSignInDtoRequest request) {
        if (verificationService.isMatchVerificationCode(request.phoneOrEmail(), request.verificationCode())) {
            return String.format("This successful completed: %s", request.phoneOrEmail());
        }
        return String.format("This not successful completed: %s", request.phoneOrEmail()); //TODO change response
    }

    public AuthDtoResponse register(UserDtoCreateRequest request) {
        var user = userMapper.mapFrom(request);
        var createdUser = userService.create(user);
        return new AuthDtoResponse(createdUser.toString(), "This successful"); //TODO create security
    }

}

package com.vlinvestment.accountservice.facade;

import com.vlinvestment.accountservice.dto.request.AuthDtoRequest;
import com.vlinvestment.accountservice.dto.request.UserDtoCreateRequest;
import com.vlinvestment.accountservice.dto.request.UserSignInDtoRequest;
import com.vlinvestment.accountservice.dto.response.AuthDtoResponse;
import com.vlinvestment.accountservice.mapper.UserMapper;
import com.vlinvestment.accountservice.service.AccessCodeService;
import com.vlinvestment.accountservice.service.UserService;
import com.vlinvestment.accountservice.service.messaging.SenderVerificationCodeService;
import com.vlinvestment.accountservice.service.security.JwtService;
import com.vlinvestment.accountservice.service.security.impl.UserDetailsImpl;
import com.vlinvestment.accountservice.verification.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final SenderVerificationCodeService senderVerificationCodeService;
    private final UserMapper userMapper;
    private final VerificationService verificationService;
    private final AccessCodeService accessCodeService;

    public AuthDtoResponse login(UserSignInDtoRequest request) {
        verificationService.verifyUserRegister(request.phoneOrEmail());
        verificationService.verifyCode(request.phoneOrEmail(), request.verificationCode());

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.phoneOrEmail(),
                        request.verificationCode()
                )
        );
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();

        var jwt = jwtService.generateToken(userDetails.getUsername());
        return new AuthDtoResponse(
                request.phoneOrEmail(),
                jwt
        );
    }

    public String sendVerificationCode(AuthDtoRequest request) {
        verificationService.verifyUserRegister(request.phoneOrEmail());
        verificationService.verifyRepeatSend(request.phoneOrEmail());
        return senderVerificationCodeService.sendVerificationCode(request.messagingSource(), request.phoneOrEmail());
    }

    public String completedPhoneOrEmail(UserSignInDtoRequest request) {
        verificationService.verifyCode(request.phoneOrEmail(), request.verificationCode());
        return String.format("This %s successful completed", request.phoneOrEmail());
    }

    public AuthDtoResponse register(UserDtoCreateRequest request) {
        verificationService.verifyCompletedPhoneOrEmail(request.phone(), request.email());
        var user = userMapper.mapFrom(request);
        userService.create(user); // TODO add roles
        accessCodeService.delete(request.phone());
        var jwt = jwtService.generateToken(request.phone());
        return new AuthDtoResponse(
                request.phone(),
                jwt
        );
    }

}

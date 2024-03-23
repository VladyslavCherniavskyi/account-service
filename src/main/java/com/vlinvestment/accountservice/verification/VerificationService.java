package com.vlinvestment.accountservice.verification;

import com.vlinvestment.accountservice.exeption.VerificationCodeException;
import com.vlinvestment.accountservice.service.AccessCodeService;
import com.vlinvestment.accountservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VerificationService {

    private final AccessCodeService accessCodeService;
    private final UserService userService;

    public void verifyCode(String phoneOrEmail, String comparisonCode) {
        Optional.of(accessCodeService.findCodeBySource(phoneOrEmail))
                .filter(c -> !Objects.equals(c, comparisonCode))
                .ifPresent(c -> {
                            throw new VerificationCodeException(
                                    String.format("This verification code: %s is incorrect", comparisonCode)
                            );
                        }
                );
    }

    public void checkUserRegister(String phoneOrEmail) {
        Optional.of(!userService.existBySource(phoneOrEmail))
                .filter(i-> !i)
//                .map(Object::toString)
                .ifPresent(c -> {
                            throw new VerificationCodeException(
                                    String.format("User %s is not register", phoneOrEmail)
                            );
                        }
                );
    }

}

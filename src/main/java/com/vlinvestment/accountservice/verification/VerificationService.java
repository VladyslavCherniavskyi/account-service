package com.vlinvestment.accountservice.verification;

import com.vlinvestment.accountservice.exeption.VerificationCodeException;
import com.vlinvestment.accountservice.service.AccessCodeService;
import com.vlinvestment.accountservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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

    public void verifyUserRegister(String phoneOrEmail) {
        Optional.of(userService.existBySource(phoneOrEmail))
                .filter(c -> !c)
                .ifPresent(c -> {
                            throw new VerificationCodeException(
                                    String.format("User %s is not register", phoneOrEmail)
                            );
                        }
                );
    }

    public void verifyRepeatSend(String phone) {
        if (accessCodeService.existBySource(phone)) {
            throw new VerificationCodeException(
                    "You can send the verification code once within 1 minute. Try later"
            );
        }
    }

    public void verifyCompletedPhoneOrEmail(String phone, String email) {
        Stream.of(accessCodeService.existBySource(phone), accessCodeService.existBySource(email))
                .filter(t -> t)
                .findAny()
                .orElseThrow(() -> new VerificationCodeException("You should completed your phone or email"));
    }

}

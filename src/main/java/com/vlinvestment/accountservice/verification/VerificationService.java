package com.vlinvestment.accountservice.verification;

import com.vlinvestment.accountservice.service.AccessCodeService;
import com.vlinvestment.accountservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class VerificationService {

    private final AccessCodeService accessCodeService;
    private final UserService userService;

    public boolean isMatchSource(String source) {
        return Stream.of(userService.existBySource(source), accessCodeService.existBySource(source))
                .allMatch(isExist -> isExist);

    }

    public boolean isMatchVerificationCode(String source, String comparisonCode) {
        var code = accessCodeService.findCodeBySource(source);
        return Objects.equals(code, comparisonCode);
    }

}

package com.vlinvestment.accountservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserLoginDtoRequest(

        @NotBlank(message = "Phone cannot be empty")
        @Pattern(
                regexp = "^\\+[0-9]*$",
                message = "Phone must be a valid numeric value, optionally starting with a '+'"
        )
        String phone,

        @NotBlank(message = "VerificationCode cannot be empty")
        String verificationCode

) {
}

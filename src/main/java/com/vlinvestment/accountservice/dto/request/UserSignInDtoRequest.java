package com.vlinvestment.accountservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserSignInDtoRequest(

        @NotBlank(message = "This field cannot be empty")
        @Pattern(
                regexp = "^[0-9]*$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,6}$",
                message = "Invalid phone number or email format."
        )
        String phoneOrEmail,

        @NotBlank(message = "VerificationCode cannot be empty")
        String verificationCode

) {
}

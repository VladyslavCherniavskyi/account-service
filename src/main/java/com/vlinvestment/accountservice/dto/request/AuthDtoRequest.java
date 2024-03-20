package com.vlinvestment.accountservice.dto.request;

import com.vlinvestment.accountservice.service.messaging.MessagingSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AuthDtoRequest(

        @NotBlank(message = "This field cannot be empty")
        @Pattern(
                regexp = "^[0-9]*$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,6}$",
                message = "Invalid phone number or email format."
        )
        String phoneOrEmail,

        @NotNull(message = "MessagingSource cannot be null")
        MessagingSource messagingSource

) {
}

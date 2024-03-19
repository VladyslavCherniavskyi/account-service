package com.vlinvestment.accountservice.dto.request;

import com.vlinvestment.accountservice.service.messaging.MessagingSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LoginPhoneDtoRequest(

        @NotBlank(message = "Phone cannot be empty")
        @Pattern(
                regexp = "^[0-9]*$",
                message = "Phone must be a valid numeric value without '+'"
        )
        String phone,

        @NotNull(message = "MessagingSource cannot be null")
        MessagingSource messagingSource

) {
}

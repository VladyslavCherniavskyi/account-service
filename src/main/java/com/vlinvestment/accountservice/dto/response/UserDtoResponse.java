package com.vlinvestment.accountservice.dto.response;

public record UserDtoResponse(

        Long id,
        String firstName,
        String lastName,
        String phone,
        String email

) {
}

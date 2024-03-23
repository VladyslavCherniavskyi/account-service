package com.vlinvestment.accountservice.service.security;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {

    String generateToken(String username);

    boolean validateJwtToken(String token);

    String getToken(HttpServletRequest httpServletRequest);

    String extractUserName(String token);

}

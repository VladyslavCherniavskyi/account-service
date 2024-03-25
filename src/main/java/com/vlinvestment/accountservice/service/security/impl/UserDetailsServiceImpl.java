package com.vlinvestment.accountservice.service.security.impl;

import com.vlinvestment.accountservice.exeption.VerificationCodeException;
import com.vlinvestment.accountservice.service.UserService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Try.of(() -> userService.readByPhone(username))
                .recoverWith(throwable -> Try.of(
                        () -> userService.readByEmail(username))
                )
                .map(UserDetailsImpl::new)
                .getOrElseThrow(
                        ex -> new VerificationCodeException(ex.getMessage())
                );
    }
}

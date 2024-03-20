package com.vlinvestment.accountservice.controller;

import com.vlinvestment.accountservice.dto.request.AuthDtoRequest;
import com.vlinvestment.accountservice.dto.request.UserDtoCreateRequest;
import com.vlinvestment.accountservice.dto.request.UserSignInDtoRequest;
import com.vlinvestment.accountservice.dto.response.AuthDtoResponse;
import com.vlinvestment.accountservice.facade.AuthFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/send_code")
    public ResponseEntity<String> sendConfirmationCode(@RequestBody @Valid AuthDtoRequest request) {
        return new ResponseEntity<>(authFacade.sendVerificationCode(request), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtoResponse> login(@RequestBody @Valid UserSignInDtoRequest request) {
        return new ResponseEntity<>(authFacade.login(request), HttpStatus.OK);
    }

    @PostMapping("/source_completed")
    public ResponseEntity<String> completedPhoneOrEmail(@RequestBody @Valid UserSignInDtoRequest request) {
        return new ResponseEntity<>(authFacade.completedPhoneOrEmail(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDtoResponse> register(@RequestBody @Valid UserDtoCreateRequest request) {
        return new ResponseEntity<>(authFacade.register(request), HttpStatus.OK);
    }

}

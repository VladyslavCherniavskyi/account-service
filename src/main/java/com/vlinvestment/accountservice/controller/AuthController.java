package com.vlinvestment.accountservice.controller;

import com.vlinvestment.accountservice.dto.request.LoginPhoneDtoRequest;
import com.vlinvestment.accountservice.dto.request.UserLoginDtoRequest;
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
    public ResponseEntity<String> sendConfirmationCode(@RequestBody @Valid LoginPhoneDtoRequest request) {
        return new ResponseEntity<>(authFacade.sendVerificationCode(request), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtoResponse> login(@RequestBody @Valid UserLoginDtoRequest request) {
        return ResponseEntity.ok(authFacade.login(request));
    }


//    @PostMapping("/register/telegram")
//    public ResponseEntity<AuthDtoResponse> registerCustomer(@RequestBody @Valid UserDtoCreateRequest request) {
//        return ResponseEntity.ok(authFacade.registerByTelegramBot(new Update(), request));
//    }

}

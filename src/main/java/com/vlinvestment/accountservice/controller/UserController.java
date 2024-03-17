package com.vlinvestment.accountservice.controller;

import com.vlinvestment.accountservice.dto.request.UserDtoCreateRequest;
import com.vlinvestment.accountservice.dto.response.UserDtoResponse;
import com.vlinvestment.accountservice.facade.UserFacade;
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
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserFacade userFacade;

    @PostMapping
    public ResponseEntity<UserDtoResponse> create(@RequestBody @Valid UserDtoCreateRequest userDtoCreateRequest) {
        return new ResponseEntity<>(userFacade.create(userDtoCreateRequest), HttpStatus.OK);
    }

}

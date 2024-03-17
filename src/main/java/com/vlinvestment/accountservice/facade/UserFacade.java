package com.vlinvestment.accountservice.facade;

import com.vlinvestment.accountservice.dto.request.UserDtoCreateRequest;
import com.vlinvestment.accountservice.dto.response.UserDtoResponse;
import com.vlinvestment.accountservice.mapper.UserMapper;
import com.vlinvestment.accountservice.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserServiceImpl userServiceImpl;
    private final UserMapper userMapper;

    public UserDtoResponse create(UserDtoCreateRequest userDtoCreateRequest) {
        var user = userMapper.mapFrom(userDtoCreateRequest);
        var createdUser = userServiceImpl.create(user);
        return userMapper.mapTo(createdUser);
    }
}

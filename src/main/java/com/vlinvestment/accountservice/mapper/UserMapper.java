package com.vlinvestment.accountservice.mapper;

import com.vlinvestment.accountservice.dto.request.UserDtoCreateRequest;
import com.vlinvestment.accountservice.dto.response.UserDtoResponse;
import com.vlinvestment.accountservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    User mapFrom(UserDtoCreateRequest userDtoCreateRequest);

    UserDtoResponse mapTo(User createdUser);

}

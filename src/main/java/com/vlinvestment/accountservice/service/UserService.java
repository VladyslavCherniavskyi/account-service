package com.vlinvestment.accountservice.service;

import com.vlinvestment.accountservice.entity.User;

public interface UserService {

    User create(User user);

    User readByPhone(String phone);

    boolean existBySource(String source);

    void setPasswordHash(String phone, String code);
}

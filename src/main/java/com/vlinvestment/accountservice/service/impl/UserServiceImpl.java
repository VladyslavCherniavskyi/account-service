package com.vlinvestment.accountservice.service.impl;

import com.vlinvestment.accountservice.entity.User;
import com.vlinvestment.accountservice.repository.UserRepository;
import com.vlinvestment.accountservice.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    public User readByPhone(String phone) {
        return userRepository.findByPhone(phone).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("User with phone:%s is not found", phone)
                )
        );
    }
}

package com.vlinvestment.accountservice.service.impl;

import com.vlinvestment.accountservice.entity.User;
import com.vlinvestment.accountservice.repository.UserRepository;
import com.vlinvestment.accountservice.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

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

    @Override
    public boolean existBySource(String source) {
        return Stream.of(userRepository.existsByPhone(source), userRepository.existsByEmail(source))
                .anyMatch(isExist -> isExist);
    }

    @Override
    public void setPasswordHash(String phone, String code) {
        var user = readByPhone(phone); //TODO add email
        user.setPasswordHash(new BCryptPasswordEncoder().encode(code));
    }
}

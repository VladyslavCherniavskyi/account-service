package com.vlinvestment.accountservice.repository;

import com.vlinvestment.accountservice.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramRepository extends JpaRepository<TelegramUser, Long> {
    Optional<TelegramUser> findByPhone(String phone);

    boolean existsByPhone(String phone);
}

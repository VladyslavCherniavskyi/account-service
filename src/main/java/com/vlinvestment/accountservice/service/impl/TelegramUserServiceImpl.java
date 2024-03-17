package com.vlinvestment.accountservice.service.impl;

import com.vlinvestment.accountservice.entity.TelegramUser;
import com.vlinvestment.accountservice.repository.TelegramRepository;
import com.vlinvestment.accountservice.service.TelegramUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramRepository telegramRepository;

    public TelegramUser createTelegramUser(TelegramUser telegramUser) {
        verificationExists(telegramUser);
        return telegramRepository.save(telegramUser);
    }

    @Override
    public Boolean existByChatId(Long chatId) {
        return telegramRepository.existsById(chatId);
    }

    public TelegramUser readByPhone(String phone) {
        return telegramRepository.findByPhone(phone).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("TelegramBotUser with phone:%s is not found", phone)
                )
        );

    }

    private void verificationExists(TelegramUser telegramUser) {
        var existsByPhone = telegramRepository.existsByPhone(telegramUser.getPhone());
        if (existsByPhone) {
            throw new ValidationException(String.format("TelegramUser with phone:%s already exists", telegramUser.getPhone()));
        }
    }
}

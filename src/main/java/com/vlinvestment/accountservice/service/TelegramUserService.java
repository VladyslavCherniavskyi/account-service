package com.vlinvestment.accountservice.service;

import com.vlinvestment.accountservice.entity.TelegramUser;

public interface TelegramUserService {

    TelegramUser create(TelegramUser telegramUser);

    boolean existByChatId(Long chatId);

    TelegramUser readByPhone(String phone);

    boolean existByPhone(String phone);

}

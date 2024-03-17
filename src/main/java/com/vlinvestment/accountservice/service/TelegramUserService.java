package com.vlinvestment.accountservice.service;

import com.vlinvestment.accountservice.entity.TelegramUser;

public interface TelegramUserService {

    TelegramUser createTelegramUser(TelegramUser telegramUser);

    Boolean existByChatId(Long chatId);
}

package com.vlinvestment.accountservice.service.messaging;

public interface TelegramBot {

    void sendMessage(String phone, String message);

    void sendVerificationCode(String phone, String code);

}

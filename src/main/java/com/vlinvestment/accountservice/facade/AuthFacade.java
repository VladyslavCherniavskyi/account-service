package com.vlinvestment.accountservice.facade;

import com.vlinvestment.accountservice.dto.request.UserDtoCreateRequest;
import com.vlinvestment.accountservice.dto.request.UserLoginDtoRequest;
import com.vlinvestment.accountservice.dto.response.AuthDtoResponse;
import com.vlinvestment.accountservice.entity.TelegramUser;
import com.vlinvestment.accountservice.mapper.UserMapper;
import com.vlinvestment.accountservice.service.impl.TelegramBot;
import com.vlinvestment.accountservice.service.impl.TelegramUserServiceImpl;
import com.vlinvestment.accountservice.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final UserServiceImpl userServiceImpl;
    private final TelegramUserServiceImpl telegramUserServiceImpl;
    private final TelegramBot telegramBot;
    private final UserMapper userMapper;

    public AuthDtoResponse login(UserLoginDtoRequest request) {
        var code = request.verificationCode();
        System.out.println(code);
        return new AuthDtoResponse("User", "This not successful");
    }

    public AuthDtoResponse registerByTelegramBot(UserDtoCreateRequest request) {
        var key = "100-";
        var chatId = 1L;

        var user = userMapper.mapFrom(request);
        var telegramBotUser = TelegramUser.builder()
                .chatId(chatId)
                .phone(request.phone())
                .build();
        userServiceImpl.create(user);
        telegramUserServiceImpl.createTelegramUser(telegramBotUser);
        return new AuthDtoResponse(request.phone(), String.valueOf(chatId));
    }

    public void sendVerificationCode(String phone) {
        var chatId = telegramUserServiceImpl.readByPhone(phone).getChatId();
        telegramBot.sendMessage(chatId);
    }
}

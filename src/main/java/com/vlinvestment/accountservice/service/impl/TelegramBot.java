package com.vlinvestment.accountservice.service.impl;

import com.vlinvestment.accountservice.entity.TelegramUser;
import com.vlinvestment.accountservice.exeption.TelegramException;
import com.vlinvestment.accountservice.service.TelegramUserService;
import com.vlinvestment.accountservice.service.impl.TelegramUserServiceImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final String START = "/start";
    private static final String SHARE_CONTACT = "957685e2-2907-4c55-b738-045471f8e90a";
    private static final String DEFAULT_MASSAGE = """
            Unknown team.
            Please share contact or send
            "Get Code" to get the code 🚀
            """;

    private final TelegramUserService telegramUserServiceImpl;
    @Value("${telegram.bot.username}")
    private String botUsername;

    public TelegramBot(@Value("${telegram.bot.token}") String botToken, TelegramUserServiceImpl telegramUserServiceImpl) {
        super(botToken);
        this.telegramUserServiceImpl = telegramUserServiceImpl;
    }

    public void sendMessage(Long chatId) {
        sender(chatId, massage());
    }

    @Override
    public void onUpdateReceived(Update update) {
        var message = update.getMessage();
        var massage = message.getText();
        var chatId = message.getChatId();

        if (Objects.isNull(massage)) {
            try {
                message.getContact().getPhoneNumber();
            } catch (NullPointerException e) {
                sender(chatId, DEFAULT_MASSAGE);
            }
            message.setText(SHARE_CONTACT);
            massage = message.getText();
        }

        switch (massage) {
            case SHARE_CONTACT -> createTelegramUser(message);
            case START -> startCommand(chatId);
            default -> sender(chatId, DEFAULT_MASSAGE);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void sender(Long chatId, String massage) {
        var chatIdStr = String.valueOf(chatId);
        var sendMassage = new SendMessage(chatIdStr, massage);
        executeSend(sendMassage);
    }

    private String massage() {
        var number = IntStream.generate(() -> (int) (Math.random() * 9000) + 1000).findFirst().getAsInt();
        return "You verification code: " + number;
    }

    private void startCommand(Long chatId) {
        var exist = telegramUserServiceImpl.existByChatId(chatId);
        var notExistMessage = "You should share your contact \uD83D\uDE80";
        var existMessage = "There is no code for this user \uD83D\uDE48";
        if (exist) {
            sender(chatId, existMessage);
        } else {
            executeSend(SendMessage.builder()
                    .chatId(chatId)
                    .text(notExistMessage)
                    .replyMarkup(createContactKeyboard())
                    .build()
            );
        }
    }

    private ReplyKeyboardMarkup createContactKeyboard() {
        var contactButton = KeyboardButton.builder()
                .text("Share contact")
                .requestContact(true)
                .build();

        var row = new KeyboardRow();
        row.add(contactButton);

        var keyboard = new ArrayList<KeyboardRow>();
        keyboard.add(row);

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboard)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();
    }
    
    private void createTelegramUser(Message message) {
        removeKeyboard(message);
        var phoneNumber = message.getContact().getPhoneNumber();
        telegramUserServiceImpl.createTelegramUser(
                TelegramUser.builder()
                        .chatId(message.getChatId())
                        .phone(phoneNumber)
                        .build()
        );
    }

    private void removeKeyboard(Message message) {
        var editedMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text("Your phone number is valid")
                .replyMarkup(ReplyKeyboardRemove.builder()
                        .removeKeyboard(true)
                        .build())
                .build();
        executeSend(editedMessage);
    }

    private <T extends Serializable, Method extends BotApiMethod<T>> void executeSend(Method method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            throw new TelegramException("You cannot execute this method");
        }
    }
}

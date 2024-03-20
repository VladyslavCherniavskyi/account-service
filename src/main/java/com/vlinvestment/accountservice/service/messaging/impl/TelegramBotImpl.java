package com.vlinvestment.accountservice.service.messaging.impl;

import com.vlinvestment.accountservice.entity.AccessCode;
import com.vlinvestment.accountservice.entity.TelegramUser;
import com.vlinvestment.accountservice.exeption.TelegramException;
import com.vlinvestment.accountservice.service.AccessCodeService;
import com.vlinvestment.accountservice.service.TelegramUserService;
import com.vlinvestment.accountservice.service.messaging.TelegramBot;
import com.vlinvestment.accountservice.utils.GeneratorCodeUtil;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.concurrent.Executors;


@Service
@Transactional
public class TelegramBotImpl extends TelegramLongPollingBot implements TelegramBot {

    private static final String START = "/start";
    private static final String SHARE_CONTACT = "957685e2-2907-4c55-b738-045471f8e90a";
    private static final String DEFAULT_MASSAGE = """
            --- Unknown command ---
            Please, "share contact"
            or send "Get Code"
            to get the code 🚀
            """;

    private final TelegramUserService telegramUserService;
    private final AccessCodeService accessCodeService;
    @Value("${telegram.bot.username}")
    private String botUsername;

    public TelegramBotImpl(
            @Value("${telegram.bot.token}") String botToken,
            TelegramUserService telegramUserService,
            AccessCodeService accessCodeService) {
        super(botToken);
        this.telegramUserService = telegramUserService;
        this.accessCodeService = accessCodeService;
    }

    @Override
    public void sendMessage(String phone, String message) {
        var chatId = telegramUserService.readByPhone(phone).getChatId();
        sender(chatId, message);
    }

    @Override
    public void sendVerificationCode(String phone, String code) {
        var text = String.format("Your verification code: %s", code);
        var chatId = telegramUserService.readByPhone(phone).getChatId();
        sender(chatId, text);
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

    private void startCommand(Long chatId) {
        var exist = telegramUserService.existByChatId(chatId);
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

    private void sender(Long chatId, String message) {
        executeSend(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build()
        );
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
        telegramUserService.create(
                TelegramUser.builder()
                        .chatId(message.getChatId())
                        .phone(phoneNumber)
                        .build()
        );
        sendAndSaveAccessCode(phoneNumber);
    }

    private void sendAndSaveAccessCode(String phoneNumber) {
        var code = GeneratorCodeUtil.generateNumber();
        var executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> accessCodeService.create(
                        AccessCode.builder()
                                .source(phoneNumber)
                                .code(code)
                                .build()
                )
        );
        executorService.execute(() -> sendVerificationCode(phoneNumber, code));
        executorService.shutdown();
    }

    private void removeKeyboard(Message message) {
        executeSend(SendMessage.builder()
                .chatId(message.getChatId())
                .text("Your phone number is valid")
                .replyMarkup(ReplyKeyboardRemove.builder()
                        .removeKeyboard(true)
                        .build())
                .build()
        );
    }

    private <T extends Serializable, Method extends BotApiMethod<T>> void executeSend(Method method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            throw new TelegramException("You cannot execute this method");
        }
    }
}

package com.isldev.base.config;

import com.isldev.base.entity.User;
import com.isldev.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author YISivlay
 */
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserService userService;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.chatId}")
    private String chatId;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString(); // Ensure this is correctly captured
            String userMessage = update.getMessage().getText();

            // Save or retrieve the user and their chatId
            User user = userService.getUserByChatId(chatId);
            if (user == null) {
                user = new User();
                user.setName("system");
                user.setEmail("system@example.com");
                user.setChatId(chatId);
                userService.saveUser(user);  // Save the user with their chatId in DB
            }

            // Respond to the user with a welcome message
            if (userMessage.equalsIgnoreCase("/start")) {
                sendMessage(chatId, "Welcome to iSLDev Bot!");
            }
        }
    }

    public void sendMessage(String chatId, String message) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }
}

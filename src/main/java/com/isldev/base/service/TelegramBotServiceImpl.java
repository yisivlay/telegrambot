package com.isldev.base.service;

import com.isldev.base.config.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;

/**
 * @author YISivlay
 */
@Service
public class TelegramBotServiceImpl implements TelegramBotService {

    private final TelegramBot telegramBot;

    @Autowired
    public TelegramBotServiceImpl(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void sendInvoiceToTelegram(byte[] invoice, String chatId) {
        try {
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId);
            sendDocument.setDocument(new InputFile(new ByteArrayInputStream(invoice), "invoice.pdf"));
            telegramBot.execute(sendDocument);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Error sending invoice to Telegram", e);
        }
    }
}

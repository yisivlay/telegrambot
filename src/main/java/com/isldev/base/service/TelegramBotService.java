package com.isldev.base.service;

/**
 * @author YISivlay
 */
public interface TelegramBotService {

    void sendInvoiceToTelegram(byte[] invoice, String chatId);

}

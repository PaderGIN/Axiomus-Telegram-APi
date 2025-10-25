package ru.glebpad.axiomustelegramapi.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface SendMessageFactory {
    SendMessage sendText(Long chatId, String text);
    SendMessage sendTextWithInlineKeyboard(Long chatId, String text, InlineKeyboardMarkup kb);
    SendMessage sendTextWithReplyKeyboard(Long chatId, String text, ReplyKeyboardMarkup kb);
}
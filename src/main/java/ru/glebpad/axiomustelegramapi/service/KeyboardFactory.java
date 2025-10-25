package ru.glebpad.axiomustelegramapi.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface KeyboardFactory {
    InlineKeyboardMarkup buildLanguageChoiceKeyboard();
    ReplyKeyboardMarkup buildMainKeyboard(String langCode);
}
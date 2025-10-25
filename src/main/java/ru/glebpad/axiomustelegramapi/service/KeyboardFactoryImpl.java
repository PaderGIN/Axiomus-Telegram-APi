package ru.glebpad.axiomustelegramapi.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class KeyboardFactoryImpl implements KeyboardFactory {

    @Override
    public InlineKeyboardMarkup buildLanguageChoiceKeyboard() {
        InlineKeyboardButton en = new InlineKeyboardButton("English 🇬🇧");
        en.setCallbackData("LANG_EN");

        InlineKeyboardButton es = new InlineKeyboardButton("Español 🇪🇸");
        es.setCallbackData("LANG_ES");

        List<List<InlineKeyboardButton>> rows = List.of(List.of(en, es));
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    @Override
    public ReplyKeyboardMarkup buildMainKeyboard(String langCode) {
        String changeLang;
        String help;
        String status;

        if ("EN".equalsIgnoreCase(langCode)) {
            changeLang = "🌐 Change language";
            help = "❓ Help / examples";
            status = "📊 My status";
        } else {
            changeLang = "🌐 Cambiar idioma";
            help = "❓ Ayuda / ejemplos";
            status = "📊 Mi estado";
        }

        KeyboardRow row1 = new KeyboardRow();
        row1.add(changeLang);
        row1.add(help);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(status);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(List.of(row1, row2));
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);

        return markup;
    }
}
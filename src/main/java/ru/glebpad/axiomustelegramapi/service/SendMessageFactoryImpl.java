package ru.glebpad.axiomustelegramapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageFactoryImpl implements SendMessageFactory {

    @Override
    public SendMessage sendText(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
    }

    @Override
    public SendMessage sendTextWithInlineKeyboard(Long chatId, String text, InlineKeyboardMarkup kb) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .replyMarkup(kb)
                .build();
    }

    @Override
    public SendMessage sendTextWithReplyKeyboard(Long chatId, String text, ReplyKeyboardMarkup kb) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .replyMarkup(kb)
                .build();
    }
}
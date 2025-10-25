package ru.glebpad.axiomustelegramapi.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;
import ru.glebpad.axiomustelegramapi.entity.enums.ConversationState;

public interface ConversationStateHandler {
    ConversationState getSupportedState();

    SendMessage handle(Update update, UserSessionEntity session);
}
package ru.glebpad.axiomustelegramapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;
import ru.glebpad.axiomustelegramapi.entity.enums.ConversationState;
import ru.glebpad.axiomustelegramapi.entity.enums.Language;

@Component
@RequiredArgsConstructor
public class AnsweringStateHandler implements ConversationStateHandler {

    private final SendMessageFactory sendMessageFactory;

    @Override
    public ConversationState getSupportedState() {
        return ConversationState.ANSWERING;
    }

    @Override
    public SendMessage handle(Update update, UserSessionEntity session) {
        Long chatId = update.getMessage().getChatId();

        return sendMessageFactory.sendText(
                chatId,
                session.getLanguage() == Language.EN
                        ? "⏳ Still working on your previous question…"
                        : "⏳ Aún estoy respondiendo a tu pregunta anterior…"
        );
    }
}
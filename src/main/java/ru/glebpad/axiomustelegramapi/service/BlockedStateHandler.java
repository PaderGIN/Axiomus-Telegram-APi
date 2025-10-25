package ru.glebpad.axiomustelegramapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;
import ru.glebpad.axiomustelegramapi.entity.enums.ConversationState;

@Component
@RequiredArgsConstructor
public class BlockedStateHandler implements ConversationStateHandler {

    private final SendMessageFactory sendMessageFactory;

    @Override
    public ConversationState getSupportedState() {
        return ConversationState.BLOCKED;
    }

    @Override
    public SendMessage handle(Update update, UserSessionEntity session) {
        Long chatId = resolveChatId(update);
        return sendMessageFactory.sendText(
                chatId,
                "🚫 Access restricted. Please contact administration."
        );
    }

    private Long resolveChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        throw new RuntimeException();
    }
}
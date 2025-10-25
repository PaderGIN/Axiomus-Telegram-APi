package ru.glebpad.axiomustelegramapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;
import ru.glebpad.axiomustelegramapi.entity.enums.ConversationState;
import ru.glebpad.axiomustelegramapi.service.ConversationStateDispatcher;
import ru.glebpad.axiomustelegramapi.service.TelegramService;
import ru.glebpad.axiomustelegramapi.service.UserSessionService;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private final UserSessionService userSessionService;
    private final ConversationStateDispatcher stateDispatcher;

    @Override
    public SendMessage handleRequest(Update update) {

        Long chatId = resolveChatId(update);
        if (chatId == null) {
            throw new IllegalArgumentException();
        }

        UserSessionEntity session = userSessionService.loadOrInit(chatId);

        if (session.isBlocked()) {
            session.setState(ConversationState.BLOCKED);
        }

        return stateDispatcher.dispatch(update, session);
    }

    private Long resolveChatId(Update update) {
        if (update.hasMessage() && update.getMessage().getChatId() != null) {
            return update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()
                && update.getCallbackQuery().getMessage() != null
                && update.getCallbackQuery().getMessage().getChatId() != null) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        return null;
    }
}

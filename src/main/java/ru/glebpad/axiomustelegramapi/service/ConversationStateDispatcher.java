package ru.glebpad.axiomustelegramapi.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;
import ru.glebpad.axiomustelegramapi.entity.enums.ConversationState;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ConversationStateDispatcher {

    private final Map<ConversationState, ConversationStateHandler> handlers = new EnumMap<>(ConversationState.class);

    public ConversationStateDispatcher(List<ConversationStateHandler> handlerList) {
        for (ConversationStateHandler h : handlerList) {
            handlers.put(h.getSupportedState(), h);
        }
    }

    public SendMessage dispatch(Update update, UserSessionEntity session) {
        ConversationStateHandler handler = handlers.get(session.getState());
        if (handler == null) {
            throw new IllegalStateException("No handler for state " + session.getState());
        }
        return handler.handle(update, session);
    }
}
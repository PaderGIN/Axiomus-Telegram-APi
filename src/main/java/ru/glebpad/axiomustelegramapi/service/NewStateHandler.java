package ru.glebpad.axiomustelegramapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;
import ru.glebpad.axiomustelegramapi.entity.enums.ConversationState;

@Component
@RequiredArgsConstructor
public class NewStateHandler implements ConversationStateHandler {

    private final UserSessionService userSessionService;
    private final SendMessageFactory sendMessageFactory;
    private final KeyboardFactory keyboardFactory;

    @Override
    public ConversationState getSupportedState() {
        return ConversationState.NEW;
    }

    @Override
    public SendMessage handle(Update update, UserSessionEntity session) {
        Long chatId = resolveChatId(update);

        session.setState(ConversationState.LANG_SELECTION);
        userSessionService.save(session);

        return sendMessageFactory.sendTextWithInlineKeyboard(
                chatId,
                """
                👋 Welcome!
                
                I’m Axiomus — your university assistant.
                Soy Axiomus — tu asistente académico.
                
                I can help with:
                • deadlines
                • documents
                • where to go in the university
                
                First, choose your language / Elige tu idioma 👇
                """,
                keyboardFactory.buildLanguageChoiceKeyboard()
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
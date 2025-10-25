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
public class LangSelectionStateHandler implements ConversationStateHandler {

    private final UserSessionService userSessionService;
    private final SendMessageFactory sendMessageFactory;
    private final KeyboardFactory keyboardFactory;

    @Override
    public ConversationState getSupportedState() {
        return ConversationState.LANG_SELECTION;
    }

    @Override
    public SendMessage handle(Update update, UserSessionEntity session) {
        Long chatId = resolveChatId(update);

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();

            switch (data) {
                case "LANG_EN" -> {
                    session.setLanguage(Language.EN);
                    session.setState(ConversationState.READY);
                    userSessionService.save(session);

                    return sendMessageFactory.sendTextWithReplyKeyboard(
                            chatId,
                            """
                            ✅ Language set: English 🇬🇧
                            
                            I’m Axiomus. You can ask me anything about the university.

                            Example:
                            • "How do I get my student card?"
                            • "Where do I request a transcript?"
                            • "What is the deadline for tuition payment?"

                            Just send your question as a normal message.

                            You can change language anytime using the button below.
                            """,
                            keyboardFactory.buildMainKeyboard("EN")
                    );
                }

                case "LANG_ES" -> {
                    session.setLanguage(Language.ES);
                    session.setState(ConversationState.READY);
                    userSessionService.save(session);

                    return sendMessageFactory.sendTextWithReplyKeyboard(
                            chatId,
                            """
                            ✅ Idioma configurado: Español 🇪🇸
                            
                            Soy Axiomus. Puedes preguntarme cualquier duda sobre la universidad.

                            Ejemplos:
                            • "¿Cómo consigo el carné de estudiante?"
                            • "¿Dónde pido el certificado de notas?"
                            • "¿Cuál es el plazo de pago de la matrícula?"

                            Escríbeme tu pregunta como un mensaje normal.

                            Puedes cambiar el idioma cuando quieras con el botón de abajo.
                            """,
                            keyboardFactory.buildMainKeyboard("ES")
                    );
                }
            }
        }

        return sendMessageFactory.sendTextWithInlineKeyboard(
                chatId,
                "🌐 Please choose language / Elige tu idioma 👇",
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

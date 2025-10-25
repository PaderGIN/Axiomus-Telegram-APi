package ru.glebpad.axiomustelegramapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;
import ru.glebpad.axiomustelegramapi.entity.enums.ConversationState;
import ru.glebpad.axiomustelegramapi.entity.enums.Language;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReadyStateHandler implements ConversationStateHandler {

    private final UserSessionService userSessionService;
    private final SendMessageFactory sendMessageFactory;
    private final KeyboardFactory keyboardFactory;
    private final InferenceJobService inferenceJobService; // сервис который пушит в RabbitMQ
    // и возвращает questionId
    @Override
    public ConversationState getSupportedState() {
        return ConversationState.READY;
    }

    @Override
    public SendMessage handle(Update update, UserSessionEntity session) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Language lang = session.getLanguage();

        if (isChangeLanguageCommand(text, lang)) {
            session.setState(ConversationState.LANG_SELECTION);
            userSessionService.save(session);

            return sendMessageFactory.sendTextWithInlineKeyboard(
                    chatId,
                    lang == Language.EN
                            ? "Choose your language 👇"
                            : "Elige tu idioma 👇",
                    keyboardFactory.buildLanguageChoiceKeyboard()
            );
        }

        if (isHelpCommand(text, lang)) {
            return sendMessageFactory.sendText(
                    chatId,
                    lang == Language.EN
                            ? """
                              You can ask things like:
                              • "How do I get my student ID?"
                              • "Where do I get a certificate of enrollment?"
                              • "What office handles residence permits for international students?"
                              • "When is the tuition deadline?"
                              """
                            : """
                              Puedes preguntarme cosas como:
                              • "¿Cómo consigo el carné de estudiante?"
                              • "¿Dónde consigo el certificado de matrícula?"
                              • "¿Quién gestiona los papeles de residencia para estudiantes internacionales?"
                              • "¿Cuál es el plazo de pago de la matrícula?"
                              """
            );
        }

        if (isStatusCommand(text, lang)) {
            return sendMessageFactory.sendText(
                    chatId,
                    lang == Language.EN
                            ? buildStatusForUserEN(session)
                            : buildStatusForUserES(session)
            );
        }

        // иначе это реальный вопрос -> создаём задачу в RabbitMQ
        String questionId = inferenceJobService.enqueueQuestion(session, text);

        session.setState(ConversationState.ANSWERING);
        session.setPendingQuestionId(UUID.fromString(questionId));
        userSessionService.save(session);

        return sendMessageFactory.sendText(
                chatId,
                lang == Language.EN
                        ? "⏳ Working on it…"
                        : "⏳ Dame un segundo…"
        );
    }

    private boolean isChangeLanguageCommand(String text, Language lang) {
        return switch (lang) {
            case EN -> "🌐 Change language".equals(text);
            case ES -> "🌐 Cambiar idioma".equals(text);
        };
    }

    private boolean isHelpCommand(String text, Language lang) {
        return switch (lang) {
            case EN -> "❓ Help / examples".equals(text);
            case ES -> "❓ Ayuda / ejemplos".equals(text);
        };
    }

    private boolean isStatusCommand(String text, Language lang) {
        return switch (lang) {
            case EN -> "📊 My status".equals(text);
            case ES -> "📊 Mi estado".equals(text);
        };
    }

    private String buildStatusForUserEN(UserSessionEntity s) {
        return """
               📊 Status
               • Language: English
               • Training participation: enabled ✅
               """;
    }

    private String buildStatusForUserES(UserSessionEntity s) {
        return """
               📊 Estado
               • Idioma: Español
               • Participación en entrenamiento: activa ✅
               """;
    }
}
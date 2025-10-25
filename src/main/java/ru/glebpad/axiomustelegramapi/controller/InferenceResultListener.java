package ru.glebpad.axiomustelegramapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.glebpad.axiomustelegramapi.config.RabbitConfig;
import ru.glebpad.axiomustelegramapi.dto.InferenceResultMessage;
import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;
import ru.glebpad.axiomustelegramapi.entity.enums.ConversationState;
import ru.glebpad.axiomustelegramapi.service.SendMessageFactory;
import ru.glebpad.axiomustelegramapi.service.UserSessionService;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InferenceResultListener {

    private final UserSessionService userSessionService;
    private final SendMessageFactory sendMessageFactory;

    @RabbitListener(queues = RabbitConfig.INFERENCE_RESULTS_QUEUE)
    public void handleInferenceResult(InferenceResultMessage message) {
        UUID questionId = message.questionId();
        String answer   = message.answerText();

        log.info("Got inference result for questionId={}", questionId);

        // Нам нужно найти юзера, который сейчас ждёт этот questionId
        // Либо ты делаешь отдельную таблицу messages/tasks,
        // либо простейший вариант — хранить pendingQuestionId прямо в сессии.
        // Давай сделаем поиск по сессии.

        Optional<UserSessionEntity> sessionOpt =
                userSessionService.findByPendingQuestionId(questionId);

        if (sessionOpt.isEmpty()) {
            log.warn("No active session found for questionId={}", questionId);
            return;
        }

        UserSessionEntity session = sessionOpt.get();
        Long chatId = session.getTelegramUserId(); // поле у тебя может называться иначе

        // отправляем ответ пользователю
        sendMessageFactory.sendText(chatId, answer);

        // переводим юзера обратно в READY
        session.setPendingQuestionId(null);
        session.setState(ConversationState.READY);
        userSessionService.save(session);
    }
}
package ru.glebpad.axiomustelegramapi.service;

import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;

public interface InferenceJobService {

    /**
     * Создаёт задачу инференса (вопрос пользователя),
     * пушит её в RabbitMQ и возвращает ID задачи.
     *
     * @return questionId (UUID в строковом формате)
     */
    String enqueueQuestion(UserSessionEntity session, String userQuestionText);
}
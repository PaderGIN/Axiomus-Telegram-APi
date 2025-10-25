package ru.glebpad.axiomustelegramapi.service;

import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserSessionService {
    UserSessionEntity loadOrInit(Long telegramUserId);
    UserSessionEntity save(UserSessionEntity session);
    Optional<UserSessionEntity> findByPendingQuestionId(UUID questionId);
}

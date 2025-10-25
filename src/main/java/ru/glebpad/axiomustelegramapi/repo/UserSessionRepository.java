package ru.glebpad.axiomustelegramapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSessionEntity, Long> {
    Optional<UserSessionEntity> findByTelegramUserId(Long telegramUserId);
    Optional<UserSessionEntity> findByPendingQuestionId(UUID pendingQuestionId);
}
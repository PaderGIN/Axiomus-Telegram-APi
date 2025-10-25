package ru.glebpad.axiomustelegramapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;
import ru.glebpad.axiomustelegramapi.entity.enums.ConversationState;
import ru.glebpad.axiomustelegramapi.repo.UserSessionRepository;
import ru.glebpad.axiomustelegramapi.service.UserSessionService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository repo;

    public UserSessionServiceImpl(UserSessionRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserSessionEntity loadOrInit(Long telegramUserId) {
        try {
            UserSessionEntity userSessionEntity = repo.findByTelegramUserId(telegramUserId)
                    .orElseGet(() -> {
                        UserSessionEntity fresh = new UserSessionEntity();
                        fresh.setTelegramUserId(telegramUserId);
                        fresh.setState(ConversationState.NEW);
                        fresh.setLanguage(null);
                        fresh.setPendingQuestionId(null);
                        fresh.setBlocked(false);
                        fresh.setLangChangeRequested(false);
                        fresh.setLastInteractionAt(Instant.now());
                        return repo.save(fresh);
                    });
            log.info("Session loaded: {}", userSessionEntity.getTelegramUserId());
            return userSessionEntity;
        } catch (Exception e) {
            log.error("Error loading user session", e);
            throw e;
        }
    }

    @Override
    public UserSessionEntity save(UserSessionEntity session) {
        session.setLastInteractionAt(Instant.now());
        return repo.save(session);
    }

    @Override
    public Optional<UserSessionEntity> findByPendingQuestionId(UUID questionId) {
        return repo.findByPendingQuestionId(questionId);
    }

    public void updateState(Long telegramUserId, ConversationState newState) {
        var s = loadOrInit(telegramUserId);
        s.setState(newState);
        save(s);
    }

    // etc: setLanguage(...), setPendingQuestionId(...), markBlocked(...)
}
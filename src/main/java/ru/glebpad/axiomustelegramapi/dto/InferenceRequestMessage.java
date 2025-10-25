package ru.glebpad.axiomustelegramapi.dto;

import java.time.Instant;
import java.util.UUID;

public record InferenceRequestMessage(
        UUID questionId,        // уникальный ID задачи
        Long userId,            // telegram chatId
        String language,        // "EN" / "ES"
        String questionText,    // сам вопрос юзера
        Instant createdAt       // для трассировки и SLA
) {}
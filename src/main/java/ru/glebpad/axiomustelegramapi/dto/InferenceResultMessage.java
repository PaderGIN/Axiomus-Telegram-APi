package ru.glebpad.axiomustelegramapi.dto;

import java.util.UUID;

public record InferenceResultMessage(
        UUID questionId,     // тот же самый ID
        String answerText    // готовый ответ модели
) {}
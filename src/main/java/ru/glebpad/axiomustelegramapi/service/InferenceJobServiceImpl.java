package ru.glebpad.axiomustelegramapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.glebpad.axiomustelegramapi.config.RabbitConfig;
import ru.glebpad.axiomustelegramapi.dto.InferenceRequestMessage;
import ru.glebpad.axiomustelegramapi.entity.UserSessionEntity;
import ru.glebpad.axiomustelegramapi.entity.enums.Language;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InferenceJobServiceImpl implements InferenceJobService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public String enqueueQuestion(UserSessionEntity session, String userQuestionText) {

        UUID questionId = UUID.randomUUID();

        InferenceRequestMessage payload = new InferenceRequestMessage(
                questionId,
                session.getTelegramUserId(), // см. как поле называется у тебя в UserSessionEntity
                languageToCode(session.getLanguage()),
                userQuestionText,
                Instant.now()
        );

        // RabbitTemplate по дефолту использует Jackson2JsonMessageConverter
        // если мы его сконфигурим — это обычно делается 1 раз в @Configuration
        rabbitTemplate.convertAndSend(
                RabbitConfig.INFERENCE_REQUESTS_QUEUE,
                payload
        );

        log.info("Enqueued inference request questionId={} userId={} lang={} text={}",
                questionId, session.getTelegramUserId(), session.getLanguage(), userQuestionText);

        return questionId.toString();
    }

    private String languageToCode(Language lang) {
        return switch (lang) {
            case EN -> "EN";
            case ES -> "ES";
        };
    }
}
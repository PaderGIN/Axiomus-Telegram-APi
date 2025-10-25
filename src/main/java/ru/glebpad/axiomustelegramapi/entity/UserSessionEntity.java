package ru.glebpad.axiomustelegramapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.glebpad.axiomustelegramapi.entity.enums.ConversationState;
import ru.glebpad.axiomustelegramapi.entity.enums.Language;

import javax.annotation.processing.Generated;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "user_sessions")
public class UserSessionEntity {
    @Id
    private Long telegramUserId;

    @Enumerated(EnumType.STRING)
    private ConversationState state;

    @Enumerated(EnumType.STRING)
    private Language language; // "EN", "ES", etc.

    @Column(name = "pending_question_id")
    private UUID pendingQuestionId;

    private boolean blocked;

    private boolean langChangeRequested;

    private Instant lastInteractionAt;
}
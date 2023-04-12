package ru.practicum.explore_with_me.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.model.StatusRequest;

import java.time.LocalDateTime;

/**
 * Заявка на участие в событии.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class ParticipationRequestDto {
    /**
     * Идентификатор заявки.
     */
    private Long id;
    /**
     * Дата и время создания заявки.
     */
    private LocalDateTime created;
    /**
     * Идентификатор события.
     */
    private Long event;
    /**
     * Идентификатор пользователя, отправившего заявку.
     */
    private Long requester;
    /**
     * Статус заявки.
     */
    private StatusRequest status;
}

package ru.practicum.explore_with_me.model;

import lombok.Getter;

/**
 * Статус заявки на участие в событии.
 */
@Getter
public enum StatusRequest {
    /**
     * подтверждено
     */
    CONFIRMED,
    /**
     * в ожидании
     */
    PENDING,
    /**
     * опубликовано
     */
    PUBLISHED,
    /**
     * отменено
     */
    CANCELED
}

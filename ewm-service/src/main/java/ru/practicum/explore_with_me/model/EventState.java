package ru.practicum.explore_with_me.model;

import lombok.Getter;

/**
 * Статус события.
 */
@Getter
public enum EventState {
    /**
     * Событие в ожидании.
     */
    PENDING,
    /**
     * Событие опубликовано.
     */
    PUBLISHED,
    /**
     * Событие отменено.
     */
    CANCELED
}

package ru.practicum.explore_with_me.handler.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Ошибка 400. Ошибка в сортировке запроса и т.п.
 */
@Slf4j
public class InvalidSortException extends RuntimeException {
    public InvalidSortException(String message) {
        super(message);
        log.error(message);
    }
}

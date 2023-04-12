package ru.practicum.explore_with_me.handler.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Ошибка 400. Ошибка во времени и т.п.
 */
@Slf4j
public class InvalidDateTimeException extends RuntimeException {
    public InvalidDateTimeException(String message) {
        super(message);
        log.error(message);
    }
}

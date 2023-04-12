package ru.practicum.explore_with_me.handler.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Ошибка 409. Ошибка операции из-за данных и т.п.
 */
@Slf4j
public class OperationFailedException extends RuntimeException {
    public OperationFailedException(String message) {
        super(message);
        log.error(message);
    }
}

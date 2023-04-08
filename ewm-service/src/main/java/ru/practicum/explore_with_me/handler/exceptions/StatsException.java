package ru.practicum.explore_with_me.handler.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Ошибка 409. Исключение при ошибке сервера статистики.
 */
@Slf4j
public class StatsException extends RuntimeException {
    public StatsException(String message) {
        super(message);
        log.error(message);
    }
}

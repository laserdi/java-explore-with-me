package ru.practicum.explore_with_me.handler.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Ошибка 409. Ошибка операции из-за отсутствия данных и т.п.
 */
@Slf4j
public class NotFoundRecordInBD extends RuntimeException {
    public NotFoundRecordInBD(String message) {
        super(message);
        log.error(message);
    }
}

package ru.practicum.explore_with_me.handler.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Ошибка 400. Исключение при ошибке значений полей объектов.
 */
@Slf4j
public class WrongFieldValueException extends RuntimeException {
    public WrongFieldValueException(String message) {
        super(message);
        log.error(message);
    }
}

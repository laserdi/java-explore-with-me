package ru.practicum.explore_with_me.handler.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Ошибка 409. Ошибка операции из-за конфликта данных в БД и т.п.
 */
@Slf4j
public class FoundConflictInDB extends RuntimeException {
    public FoundConflictInDB(String message) {
        super(message);
        log.error(message);
    }
}

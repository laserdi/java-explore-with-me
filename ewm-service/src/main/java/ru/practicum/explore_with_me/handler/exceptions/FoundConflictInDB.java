package ru.practicum.explore_with_me.handler.exceptions;

/**
 * Ошибка 409. Ошибка операции из-за конфликта данных в БД и т.п.
 */
public class FoundConflictInDB extends RuntimeException {
    public FoundConflictInDB(String message) {
        super(message);
    }
}

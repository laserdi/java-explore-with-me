package ru.practicum.explore_with_me.handler.exceptions;

/**
 * Ошибка 409. Ошибка операции из-за данных и т.п.
 */
public class OperationFailedException extends RuntimeException {
    public OperationFailedException(String message) {
        super(message);
    }
}

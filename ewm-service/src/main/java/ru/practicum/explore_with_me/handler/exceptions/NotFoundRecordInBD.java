package ru.practicum.explore_with_me.handler.exceptions;

/**
 * Ошибка 409. Ошибка операции из-за отсутствия данных и т.п.
 */
public class NotFoundRecordInBD extends RuntimeException {
    public NotFoundRecordInBD(String message) {
        super(message);
    }
}

package ru.practicum.explore_with_me.handler.exceptions;

/**
 * Ошибка 400. Ошибка во времени и т.п.
 */
public class InvalidDateTimeException extends RuntimeException {
    public InvalidDateTimeException(String message) {
        super(message);
    }
}

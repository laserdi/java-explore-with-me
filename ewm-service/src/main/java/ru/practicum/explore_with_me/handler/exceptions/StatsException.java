package ru.practicum.explore_with_me.handler.exceptions;

/**
 * Ошибка 409. Исключение при ошибке сервера статистики.
 */
public class StatsException extends RuntimeException {
    public StatsException(String message) {
        super(message);
    }
}

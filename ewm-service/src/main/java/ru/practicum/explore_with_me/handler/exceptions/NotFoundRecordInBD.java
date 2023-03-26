package ru.practicum.explore_with_me.handler.exceptions;

public class NotFoundRecordInBD extends RuntimeException {

    public NotFoundRecordInBD(String message) {
        super(message);
    }
}

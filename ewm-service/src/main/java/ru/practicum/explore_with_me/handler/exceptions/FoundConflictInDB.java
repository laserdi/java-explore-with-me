package ru.practicum.explore_with_me.handler.exceptions;

public class FoundConflictInDB extends RuntimeException {
    public FoundConflictInDB(String message) {
        super(message);
    }
}

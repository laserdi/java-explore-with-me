package ru.practicum.explore_with_me.apierror.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundRecordInBD extends RuntimeException {

    public NotFoundRecordInBD(String message) {
        super(message);
    }
}

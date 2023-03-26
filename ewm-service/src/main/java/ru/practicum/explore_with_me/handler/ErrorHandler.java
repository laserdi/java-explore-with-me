package ru.practicum.explore_with_me.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore_with_me.handler.exceptions.FoundConflictInDB;
import ru.practicum.explore_with_me.handler.exceptions.NotFoundRecordInBD;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundRecordInBD.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundRecordInBD(final NotFoundRecordInBD ex) {
        log.info("Ошибка 404. {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("Запрашиваемая операция не может быть выполнена.")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(FoundConflictInDB.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleFoundConflictInDB(final FoundConflictInDB ex) {
        log.info("Ошибка 409. {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("Запрашиваемая операция не может быть выполнена.")
                .status(HttpStatus.CONFLICT.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }
}

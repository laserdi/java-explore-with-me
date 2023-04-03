package ru.practicum.explore_with_me.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore_with_me.handler.exceptions.*;

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
                .status(HttpStatus.NOT_FOUND.toString())
//                .status(HttpStatus.NOT_FOUND.name())  //Было так.
                .timestamp(LocalDateTime.now())
                .stackTrace(ex.getStackTrace().toString())
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
                .stackTrace(ex.getStackTrace().toString())
                .build();
        return apiError;
    }

    @ExceptionHandler(OperationFailedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleOperationFailedException(final OperationFailedException ex) {
        log.info("Ошибка 409. {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("Запрашиваемая операция не может быть выполнена.")
                .status(HttpStatus.CONFLICT.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .stackTrace(ex.getStackTrace().toString())
                .build();
        return apiError;
    }

    @ExceptionHandler(InvalidDateTimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleFoundConflictInDB(final InvalidDateTimeException ex) {
        log.info("Ошибка 409. {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("Запрашиваемая операция не может быть выполнена.")
                .status(HttpStatus.CONFLICT.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .stackTrace(ex.getStackTrace().toString())
                .build();
        return apiError;
    }

    @ExceptionHandler(StatsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleFoundConflictInDB(final StatsException ex) {
        log.info("Ошибка 409. {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("Запрашиваемая операция не может быть выполнена.")
                .status(HttpStatus.CONFLICT.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .stackTrace(ex.getStackTrace().toString())
                .build();
        return apiError;
    }
}

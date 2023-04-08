package ru.practicum.explore_with_me.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore_with_me.handler.exceptions.*;

import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundRecordInBD.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundRecordInBD(final NotFoundRecordInBD ex) {
        log.error("Ошибка 404. {}", ex.getMessage());

        ApiError apiError = new ApiError(ex.getMessage(),
                "Запрашиваемая операция не может быть выполнена.", HttpStatus.NOT_FOUND);
        return apiError;
    }

    @ExceptionHandler(FoundConflictInDB.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleFoundConflictInDB(final FoundConflictInDB ex) {
        log.error("Ошибка 409. {}", ex.getMessage());
        ApiError apiError = new ApiError(ex.getMessage(),
                "Запрашиваемая операция не может быть выполнена.",
                HttpStatus.CONFLICT);
        return apiError;
    }

    @ExceptionHandler(OperationFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleOperationFailedException(final OperationFailedException ex) {
        log.error("Ошибка 409. {}", ex.getMessage());
        ApiError apiError = new ApiError(ex.getMessage(),
                "Запрашиваемая операция не может быть выполнена.",
                HttpStatus.CONFLICT);
        return apiError;
    }

    @ExceptionHandler(InvalidDateTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidDateTimeException(final InvalidDateTimeException ex) {
        log.error("Ошибка 400. {}", ex.getMessage());
        ApiError apiError = new ApiError(ex.getMessage(),
                "Запрашиваемая операция не может быть выполнена.",
                HttpStatus.BAD_REQUEST);
        return apiError;
    }

    @ExceptionHandler(StatsException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiError handleStatsException(final StatsException ex) {
        log.error("Ошибка 503. {}", ex.getMessage());
        ApiError apiError = new ApiError(ex.getMessage(),
                "Запрашиваемая операция не может быть выполнена.",
                HttpStatus.SERVICE_UNAVAILABLE);
        return apiError;
    }

    @ExceptionHandler(BadHttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadHttpMessageNotReadableException(final BadHttpMessageNotReadableException ex) {
        log.error("Ошибка 400. {}", ex.getMessage());
        ApiError apiError = new ApiError(ex.getMessage(),
                "Запрашиваемая операция не может быть выполнена. ***BadHttpMessageNotReadableException*** ;-)",
                HttpStatus.BAD_REQUEST);
        return apiError;
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleSQLException(final SQLException ex) {
        log.error("Ошибка 409. {}", ex.getMessage());
        ApiError apiError = new ApiError(ex.getMessage(),
                "Запрашиваемая операция не может быть выполнена.",
                HttpStatus.CONFLICT);
        return apiError;
    }
}

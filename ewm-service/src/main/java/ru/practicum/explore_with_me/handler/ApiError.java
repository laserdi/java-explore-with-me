package ru.practicum.explore_with_me.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Сведения об ошибке.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ApiError {

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private List<String> errors = new ArrayList<>();
    /**
     * <p>Сообщение об ошибке.</p>
     * example: Only pending or canceled events can be changed
     */
    private String message;
    /**
     * <p>Общее описание причины ошибки</p>
     * example: For the requested operation the conditions are not met.
     */
    private String reason;
    /**
     * Код статуса HTTP-ответа.
     * <p>example: FORBIDDEN</p>
     */
    private HttpStatus status;
    /**
     * Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss").
     */
    private String timestamp;

    public ApiError(String message, String reason, HttpStatus status) {
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = LocalDateTime.now().format(DATE_TIME);
    }



}

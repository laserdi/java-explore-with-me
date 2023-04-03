package ru.practicum.explore_with_me.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Ошибка.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ApiError {
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
    private String status;

    /**
     * Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss").
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    /**
     * Путь до метода и ошибки.
     */
    private String stackTrace;
}

package ru.practicum.explore_with_me.apierror;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Ошибка.
 */
@Getter
@Setter
@NoArgsConstructor
public class ApiError {
    /**
     * Список стектрейсов или описания ошибок.
     */
    private List<String> errors;

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
}

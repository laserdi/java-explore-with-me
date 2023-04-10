package ru.practicum.explore_with_me.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor    // используется при десериализации джексоном)
@AllArgsConstructor
@Builder(toBuilder = true)
public class StatsDtoForSave {
    /**
     * {
     * <p>"app": "ewm-main-service",</p>
     * <p>"uri": "/events/1",</p>
     * <p>"ip": "192.163.0.1",</p>
     * <p>"timestamp": "2022-09-06 11:00:23"</p>
     * }
     */
    private Long id;
    @NotBlank(message = "Имя приложения должно быть не пустым.")
    private String app;
    @NotBlank(message = "Строка, по которой сохраняется обращение в БД должна быть не пустой.")
    private String uri;
    @NotBlank(message = "IP-адрес, с которого идёт запрос не должен быть пустым для сохранения в БД.")
    private String ip;
    /**
     * Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
     */
    @NotNull(message = "Отсутствует параметр 'timestamp' или он равен null DTO-объекта при сохранении " +
            "в БД обращения к ресурсу.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}

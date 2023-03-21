package ru.practicum.explore_with_me.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
//@RequiredArgsConstructor
@NoArgsConstructor    // используется при десериализации джексоном)
@AllArgsConstructor
public class StatsDtoForSave {
    /**
     * {
     * "app": "ewm-main-service",
     * "uri": "/events/1",
     * "ip": "192.163.0.1",
     * "timestamp": "2022-09-06 11:00:23"
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}

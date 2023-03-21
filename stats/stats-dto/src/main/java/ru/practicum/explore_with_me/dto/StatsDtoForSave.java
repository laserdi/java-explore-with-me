package ru.practicum.explore_with_me.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
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
    @NotNull(message = "Имя приложения должно быть не null.")
    @NotBlank(message = "Имя приложения должно быть не пустым.")
    private String app;
    @NotNull(message = "Строка, по которой сохраняется обращение в БД должна быть не null.")
    @NotBlank(message = "Строка, по которой сохраняется обращение в БД должна быть не пустой.")
    private String uri;
    @NotNull(message = "IP-адрес, с которого идёт запрос не должен быть null для сохранения в БД.")
    @NotBlank(message = "IP-адрес, с которого идёт запрос не должен быть пустым для сохранения в БД.")
    private String ip;
    /**
     * Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
     */
    @NotNull(message = "Время сохранения в БД должно быть не null.")
    @NotBlank(message = "Время сохранения в БД должно быть не пустым.")
    private String timestamp;
}

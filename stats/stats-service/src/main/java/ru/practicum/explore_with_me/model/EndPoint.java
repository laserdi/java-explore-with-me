package ru.practicum.explore_with_me.model;

import java.time.LocalDateTime;

/**
 * EndpointHit{
 * id	integer($int64)
 * readOnly: true
 * example: 1
 * Идентификатор записи
 * app	string
 * example: ewm-main-service
 * Идентификатор сервиса для которого записывается информация
 * uri	string
 * example: /events/1
 * URI для которого был осуществлен запрос
 * ip	string
 * example: 192.163.0.1
 * IP-адрес пользователя, осуществившего запрос
 * timestamp	string
 * example: 2022-09-06 11:00:23
 * Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
 */
public class EndPoint {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}

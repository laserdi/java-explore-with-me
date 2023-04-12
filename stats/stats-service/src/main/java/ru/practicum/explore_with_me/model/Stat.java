package ru.practicum.explore_with_me.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * Stat{
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
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "stats")
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stats_id", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "app_id")
    private Application app;
    @Column(name = "uri", nullable = false)
    private String uri;
    @Column(name = "ip", nullable = false)
    private String ip;
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}

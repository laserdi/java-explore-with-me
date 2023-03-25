package ru.practicum.explore_with_me.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * description:
 * Данные для изменения информации о событии. Если поле в запросе не указано (равно null) - значит изменение этих
 * данных не требуется.
 * <p>
 * annotation	string
 * maxLength: 2000
 * minLength: 20
 * example: Сап прогулки по рекам и каналам – это возможность увидеть Практикбург с другого ракурса
 * Новая аннотация
 * <p>
 * category	integer($int64)
 * example: 3
 * Новая категория
 * <p>
 * description	string
 * maxLength: 7000
 * minLength: 20
 * example: От английского SUP - Stand Up Paddle — "стоя на доске с веслом", гавайская разновидность сёрфинга,
 * в котором серфер, стоя на доске, катается на волнах и при этом гребет веслом, а не руками, как в классическом
 * серфинге.
 * Новое описание
 * <p>
 * eventDate	string
 * example: 2023-10-11 23:10:05
 * Новые дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
 * <p>
 * location	Location{...}
 * paid	boolean
 * example: true
 * Новое значение флага о платности мероприятия
 * <p>
 * participantLimit	integer($int32)
 * example: 7
 * Новый лимит пользователей
 * <p>
 * requestModeration	boolean
 * example: false
 * Нужна ли пре-модерация заявок на участие
 * <p>
 * stateAction	string
 * example: CANCEL_REVIEW
 * Изменение сотояния события
 * <p>
 * Enum:
 * Array [ 2 ]
 * title	string
 * maxLength: 120
 * minLength: 3
 * example: Сап прогулки по рекам и каналам
 * Новый заголовок
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    /**
     * Краткое описание.
     */
    @Column(name = "annotation")
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Category category;

    /**
     * Количество одобренных заявок на участие в данном событии.
     */
    @Transient  //Поле не сохраняем в БД. Оно потом вычисляется.
    private Long confirmedRequests;

    /**
     * Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss").
     */
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;


    /**
     * Полное описание события.
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss").
     */
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @Column(name = "initiator_id", nullable = false)
    private User initiator;

    /**
     * Географические координаты.
     */
    @Embedded       //Здесь внедряются сущности класса Location.
    private Location location;

    /**
     * Нужно ли оплачивать участие?
     */
    @Column(name = "paid", nullable = false)
    private Boolean paid;

    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения.
     */
    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;

    /**
     * Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss").
     */
    @Column(name = "published_on", nullable = false)
    private LocalDateTime publishedOn;

    /**
     * Нужна ли пре-модерация заявок на участие.
     */
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    /**
     * Список состояний жизненного цикла события.
     */
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonAlias({"state"})
    private EventState eventState;

    /**
     * Заголовок.
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Количество просмотрев события.
     */
    @Transient  //Поле не сохраняем в БД. Оно потом вычисляется.
    private Integer views;
}

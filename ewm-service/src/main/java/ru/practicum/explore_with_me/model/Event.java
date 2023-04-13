package ru.practicum.explore_with_me.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * description:
 * Данные для изменения информации о событии. Если поле в запросе не указано (равно null) - значит изменение этих
 * данных не требуется.
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
    @Column(name = "annotation", length = 2000)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Category category;

    /**
     * Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss").
     */
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    /**
     * Полное описание события.
     */
    @Column(name = "description", nullable = false, length = 7000)
    private String description;

    /**
     * Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss").
     */
    @Column(name = "event_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     * Организатор события.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id", nullable = false)
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
    @Column(name = "published_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
     * <p>Количество одобренных заявок на участие в данном событии.</p>
     * Поле не сохраняем в БД. Оно потом вычисляется.
     */
    @Transient  //Поле не сохраняем в БД. Оно потом вычисляется.
    private Integer confirmedRequests;

    /**
     * <p>Количество просмотров события.</p>
     * Поле не сохраняем в БД. Оно потом вычисляется.
     */
    @Transient  //Поле не сохраняем в БД. Оно потом вычисляется.
    private Integer views;

    @Transient
    private Integer comments;
}

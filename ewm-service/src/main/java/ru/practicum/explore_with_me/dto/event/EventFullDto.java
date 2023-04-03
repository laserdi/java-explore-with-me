package ru.practicum.explore_with_me.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.dto.category.CategoryDto;
import ru.practicum.explore_with_me.model.EventState;
import ru.practicum.explore_with_me.model.Location;
import ru.practicum.explore_with_me.model.User;

import javax.persistence.Embedded;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
//@NoArgsConstructor
public class EventFullDto {
    private Long id;

    /**
     * Краткое описание.
     */
    private String annotation;
    private CategoryDto category;
    /**
     * <p>Количество одобренных заявок на участие в данном событии.</p>
     * Поле не сохраняем в БД. Оно потом вычисляется.
     */
    private Integer confirmedRequests;

    /**
     * Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss").
     */
    private LocalDateTime createdOn;
    /**
     * Полное описание события.
     */
    private String description;
    /**
     * Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss").
     */
    private LocalDateTime eventDate;
    /**
     * Организатор события.
     */
    private User initiator;
    /**
     * Географические координаты.
     */
    @Embedded       //Здесь внедряются сущности класса Location.
    private Location location;
    /**
     * Нужно ли оплачивать участие?
     */
    private Boolean paid;
    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения.
     */
    private Integer participantLimit;
    /**
     * Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss").
     */
    private LocalDateTime publishedOn;
    /**
     * Нужна ли пре-модерация заявок на участие.
     */
    private Boolean requestModeration;
    /**
     * Список состояний жизненного цикла события.
     */
    @JsonProperty("state")
    private EventState eventState;
    /**
     * Заголовок.
     */
    private String title;
    /**
     * <p>Количество просмотрев события.</p>
     * Поле не сохраняем в БД. Оно потом вычисляется.
     */
    private Integer views;
}

package ru.practicum.explore_with_me.dto.event;

import lombok.*;
import ru.practicum.explore_with_me.dto.category.CategoryDto;
import ru.practicum.explore_with_me.model.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NewEventDtoForResponse {
    /**
     * Краткое описание события.
     */
    private String annotation;
    /**
     * ID категории к которой относится событие.
     */
    private CategoryDto category;
    /**
     * Полное описание события.
     */
    private String description;
    /**
     * Дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss".
     */
    private LocalDateTime eventDate;
    /**
     * Широта и долгота места проведения события.
     */
    private Location location;
    /**
     * Нужно ли оплачивать участие в событии.
     */
    private boolean paid;
    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения.
     */
    private Integer participantLimit;
    /**
     * Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения
     * инициатором события. Если false - то будут подтверждаться автоматически.
     * <p>default = true</p>
     */
    private boolean requestModeration;
    /**
     * Заголовок события.
     */
    private String title;
}
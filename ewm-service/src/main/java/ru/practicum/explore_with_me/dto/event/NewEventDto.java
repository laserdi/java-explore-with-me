package ru.practicum.explore_with_me.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.explore_with_me.validation.CreateObject;

import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class NewEventDto {
    /**
     * Краткое описание.
     */
    @NotBlank(groups = CreateObject.class)
    @Size(min = 20, max = 2000, message = "Для описания требуется от 20 до 2000 символов.", groups = CreateObject.class)
    private String annotation;
    /**
     * id категории к которой относится событие.
     */
    @JsonProperty("category")
    @NotNull(groups = CreateObject.class)
    @PositiveOrZero(groups = CreateObject.class)
    private Long categoryId;
    /**
     * Полное описание события.
     */
    @NotBlank(groups = CreateObject.class)
    @Size(min = 20, max = 7000, message = "Для описания требуется от 20 до 7000 символов.", groups = CreateObject.class)
    private String description;
    /**
     * Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss").
     * <p>Обратите внимание: дата и время на которые намечено событие не может быть раньше,
     * чем через два часа от текущего момента</p>
     */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    /**
     * Географические координаты.
     */
    @Embedded       //Здесь внедряются сущности класса Location.
    private LocationDto location;
    /**
     * Нужно ли оплачивать участие?
     */
    private boolean paid;
    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения.
     */
    @PositiveOrZero
    private int participantLimit;
    /**
     * Нужна ли пре-модерация заявок на участие.
     */
    private boolean requestModeration = true;
    /**
     * Заголовок.
     */
    @NotBlank(message = "Заголовок должен быт не null.", groups = {CreateObject.class})
    @Size(min = 3, max = 120, message = "Заголовок должен содержать от 3-х до 120 символов.",
            groups = {CreateObject.class})
    private String title;
}

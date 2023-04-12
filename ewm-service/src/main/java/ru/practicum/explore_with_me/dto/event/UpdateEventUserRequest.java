package ru.practicum.explore_with_me.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.model.StateAction;

import javax.persistence.Embedded;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Данные для изменения информации о событии. Если поле в запросе не указано (равно null) - значит изменение
 * этих данных не требуется.
 */
@Getter
@Setter
@NoArgsConstructor
public class UpdateEventUserRequest {
    /**
     * Новая аннотация.
     */
    @Size(min = 20, max = 2000)
    private String annotation;
    /**
     * Новая категория.
     */
    private Long category;
    /**
     * Новое описание.
     */
    @Size(min = 20, max = 7000)
    private String description;
    /**
     * Новые дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    /**
     * Новое значение флага о платности мероприятия.
     */
    @Embedded
    private LocationDto location;
    /**
     * Надо ли оплачивать участие в событии.
     */
    private Boolean paid;
    /**
     * Новый лимит пользователей.
     */
    @PositiveOrZero
    private Integer participantLimit;
    /**
     * Нужна ли пре-модерация заявок на участие?
     */
    private Boolean requestModeration = true;
    /**
     * Изменение состояния события.
     */
    private StateAction stateAction;
    /**
     * Новый заголовок.
     */
    @Size(min = 3, max = 120, message = "Заголовок должен содержать от 3-х до 120 символов.")
    private String title;
}

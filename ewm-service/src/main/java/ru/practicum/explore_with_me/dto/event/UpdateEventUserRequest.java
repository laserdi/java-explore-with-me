package ru.practicum.explore_with_me.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.model.Location;
import ru.practicum.explore_with_me.model.StateAction;

import java.time.LocalDateTime;

/**
 * Данные для изменения информации о событии. Если поле в запросе не указано (равно null) - значит изменение
 * этих данных не требуется.
 */
@Getter
@Setter
@NoArgsConstructor
public class UpdateEventUserRequest {
//    /**
//     * Идентификатор события.
//     */
//    private Long eventId;
    /**
     * Новая аннотация.
     */
    private String annotation;
    /**
     * Новая категория.
     */
    private Long category;
    /**
     * Новое описание.
     */
    private String description;
    /**
     * Новые дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    /**
     * Новое значение флага о платности мероприятия.
     */
    private Location location;
    /**
     * Надо ли оплачивать участие в событии.
     */
    private Boolean paid;
    /**
     * Новый лимит пользователей.
     */
    private Integer participantLimit;
    /**
     * Нужна ли пре-модерация заявок на участие?
     */
    private Boolean requestModeration;
    /**
     * Изменение состояния события.
     */
    private StateAction stateAction;
    /**
     * Новый заголовок.
     */
    private String title;
}

package ru.practicum.explore_with_me.dto.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.model.StateAction;

import java.time.LocalDateTime;

/**
 * Данные для изменения информации о событии. Если поле в запросе не указано (равно null) - значит изменение
 * этих данных не требуется.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class UpdateEventAdminRequest {

    /**
     * Новая аннотация.
     * <p>maxLength: 2000</p>
     * <p>minLength: 20</p>
     */
    private String annotation;

    /**
     * Новая категория.
     */
    private Long category;

    /**
     * Новое описание.
     * maxLength: 7000
     * minLength: 20
     */
    private String description;

    /**
     * <p>Новые дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss".</p>
     */
    private LocalDateTime eventDate;

    /**
     * Локация.
     */
    private LocationDto location;

    /**
     * Новое значение флага о платности мероприятия.
     */
    private Boolean paid;
    /**
     * Новый лимит пользователей.
     */
    private Integer participantLimit;

    /**
     * Нужна ли пре-модерация заявок на участие.
     */
    private Boolean requestModeration;
    /**
     * Новое состояние события.
     */
    private StateAction stateAction;
    /**
     * <p>Новый заголовок.</p>
     * maxLength: 120
     * <p>minLength: 3</p>
     */
    private String title;
}

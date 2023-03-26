package ru.practicum.explore_with_me.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.dto.category.CategoryDto;
import ru.practicum.explore_with_me.dto.user.UserShortDto;

import javax.persistence.Transient;
import java.time.LocalDateTime;

/**
 * Краткая информация о событии
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    /**
     * <p>Идентификатор.</p>
     * example: 1
     */

    private Long id;
    /**
     * <p>Краткое описание.</p>
     * example: Эксклюзивность нашего шоу гарантирует привлечение максимальной зрительской аудитории
     */
    private String annotation;

    /**
     * Категория события.
     */
    private CategoryDto category;

    /**
     * <p>Количество одобренных заявок на участие в данном событии.</p>
     */
    private Long confirmedRequests;

    /**
     * <p>Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss").</p>
     * example: 2024-12-31 15:10:05
     */
    private LocalDateTime eventDate;
    /**
     * <p>Пользователь (краткая информация).</p>
     * UserDto{
     */
    private UserShortDto initiator;

    /**
     * Нужно ли оплачивать участие?
     */
    private boolean paid;
    /**
     * <p>Описание.</p>
     * example: Знаменитое шоу 'Летающая кукуруза'.
     */
    private String title;
    /**
     * Количество просмотрев события.
     */
    @Transient  //Поле не сохраняем в БД. Оно потом вычисляется.
    private Integer views;

}

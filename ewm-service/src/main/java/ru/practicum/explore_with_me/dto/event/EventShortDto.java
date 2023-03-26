package ru.practicum.explore_with_me.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.dto.category.CategoryDto;

import java.time.LocalDateTime;

/**
 * description:
 * Краткая информация о событии
 * category*	CategoryDto{...}
 * confirmedRequests	integer($int64)
 * example: 5
 *
 *
 * eventDate*	string
 * example: 2024-12-31 15:10:05
 * Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
 *
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
     * initiator*	UserShortDto{
     * description:
     * Пользователь (краткая информация)
     *
     * id*	integer($int64)
     * example: 3
     * Идентификатор
     *
     * name*	string
     * example: Фёдоров Матвей
     * Имя
     *
     * }
     */
//    private
}

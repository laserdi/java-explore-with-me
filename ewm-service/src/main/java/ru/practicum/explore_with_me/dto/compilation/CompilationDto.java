package ru.practicum.explore_with_me.dto.compilation;

import ru.practicum.explore_with_me.dto.event.EventShortDto;

import java.util.List;

/**
 * description:
 * Подборка событий
 *
 * events	[...]
 * id*	integer($int64)
 * example: 1
 * Идентификатор
 *
 * pinned*	boolean
 * example: true
 * Закреплена ли подборка на главной странице сайта
 *
 * title*	string
 * example: Летние концерты
 * Заголовок подборки
 */
public class CompilationDto {
    private List<EventShortDto> event;
}

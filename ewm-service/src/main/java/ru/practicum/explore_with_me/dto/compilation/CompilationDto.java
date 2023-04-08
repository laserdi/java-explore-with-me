package ru.practicum.explore_with_me.dto.compilation;

import lombok.*;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.validation.UpdateObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Подборка событий.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDto {
    /**
     * Список событий входящих в подборку.
     */
    private List<EventShortDto> events;
    private Long id;
    /**
     * Закреплена ли подборка на главной странице сайта?
     */
    private Boolean pinned;
    /**
     * Заголовок подборки.
     */
    @NotBlank(groups = UpdateObject.class)
    @Size(min = 20, max = 200, message = "Для описания требуется от 20 до 2000 символов.", groups = UpdateObject.class)
    private String title;

}


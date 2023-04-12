package ru.practicum.explore_with_me.dto.compilation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Подборка событий при создании.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class NewCompilationDto {


    /**
     * Список идентификаторов событий входящих в подборку.
     */
    private List<Long> events;

    /**
     * Закреплена ли подборка на главной странице сайта.
     * <p>default value = false;</p>
     */
    private boolean pinned;

    /**
     * Заголовок подборки.
     */
    @NotBlank
    @Size(max = 200)
    private String title;
}

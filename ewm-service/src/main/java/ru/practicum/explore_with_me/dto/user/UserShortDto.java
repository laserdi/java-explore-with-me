package ru.practicum.explore_with_me.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Пользователь (краткая информация).
 */

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    /**
     * <p>Идентификатор.</p>
     * example: 3
     */
    private Long id;

    /**
     * <p>Имя.</p>
     * example: Фёдоров Матвей
     */
    private String name;
}

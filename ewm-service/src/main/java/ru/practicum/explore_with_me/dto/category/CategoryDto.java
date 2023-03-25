package ru.practicum.explore_with_me.dto.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.validation.CreateObject;
import ru.practicum.explore_with_me.validation.UpdateObject;
import ru.practicum.explore_with_me.validation.ViewObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

/**
 * Категория
 * id	integer($int64)
 * readOnly: true
 * name*	string
 */
@Getter
@Setter
@RequiredArgsConstructor
public class CategoryDto {
    /**
     * <p>Идентификатор категории.</p>
     * example: 1
     */
    @PositiveOrZero(groups = {UpdateObject.class}, message = "При обновлении категории необходимо передать её ID.")
    @PositiveOrZero(groups = {ViewObject.class}, message = "При запросе категории необходимо передать её ID.")
    private Long id;
    /**
     * <p>Название категории.</p>
     * example: Концерты
     */
    @NotBlank(groups = {CreateObject.class}, message = "При создании категории должно быть её название.")
    @NotBlank(groups = {UpdateObject.class}, message = "При обновлении категории необходимо передать её название.")
    private String name;
}

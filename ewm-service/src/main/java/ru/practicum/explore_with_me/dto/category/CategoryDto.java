package ru.practicum.explore_with_me.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.validation.CreateObject;
import ru.practicum.explore_with_me.validation.UpdateObject;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Категория
 * id	integer($int64)
 * readOnly: true
 * name*	string
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    /**
     * <p>Идентификатор категории.</p>
     * example: 1
     */
    private Long id;
    /**
     * <p>Название категории.</p>
     * example: Концерты
     */
    @NotNull(groups = {CreateObject.class, UpdateObject.class}, message = "Имя категории не может быть Null.")
    @Size(min = 1, max = 200, groups = {CreateObject.class}, message = "При создании категории должно быть её название.")
    @Size(min = 1, max = 200, groups = {UpdateObject.class}, message = "При обновлении категории необходимо передать её название.")
    private String name;
}

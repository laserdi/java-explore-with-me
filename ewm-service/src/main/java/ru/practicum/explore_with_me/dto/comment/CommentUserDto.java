package ru.practicum.explore_with_me.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.validation.CreateObject;
import ru.practicum.explore_with_me.validation.UpdateObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CommentUserDto {

    @PositiveOrZero(groups = CreateObject.class, message = "Для создания комментария требуется передать ID события.")
    @PositiveOrZero(groups = UpdateObject.class, message = "Для изменения комментария требуется передать ID события.")
    private Long eventId;
    @NotBlank(groups = CreateObject.class, message = "При создании комментария текст не может быть пустым " +
            "или состоять только из пробелов.")
    @NotBlank(groups = UpdateObject.class, message = "При изменении комментария текст не может быть пустым " +
            "или состоять только из пробелов.")
    @Size(min = 1, max = 2000, groups = CreateObject.class,
            message = "Для создания комментария требуется от 1 до 2000 символов.")
    @Size(min = 1, max = 2000, groups = UpdateObject.class,
            message = "Для изменения комментария требуется от 1 до 2000 символов.")
    private String text;
}

package ru.practicum.explore_with_me.dto.comment;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.explore_with_me.validation.CreateObject;
import ru.practicum.explore_with_me.validation.UpdateObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentUserDto {

    @PositiveOrZero(groups = CreateObject.class, message = "Для создания комментария требуется передать ID события.")
    @PositiveOrZero(groups = UpdateObject.class, message = "Для изменения комментария требуется передать ID события.")
    private Long eventId;
    @NotBlank(groups = CreateObject.class)
    @Size(min = 1, max = 2000, groups = CreateObject.class,
            message = "Для создания комментария требуется от 1 до 2000 символов.")
    @Size(min = 1, max = 2000, groups = UpdateObject.class,
            message = "Для изменения комментария требуется от 1 до 2000 символов.")
    private String text;
}

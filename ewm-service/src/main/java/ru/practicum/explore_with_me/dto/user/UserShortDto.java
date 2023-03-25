package ru.practicum.explore_with_me.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import ru.practicum.explore_with_me.validation.CreateObject;
import ru.practicum.explore_with_me.validation.ViewObject;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

/**
 * Пользователь (краткая информация).
 */
public class UserShortDto {
    /**
     * <p>Идентификатор.</p>
     * example: 3
     */
    @PositiveOrZero(groups = {ViewObject.class}, message = "При запросе пользователя из БД необходим его ID.")
    private Long id;

    /**
     * <p>Имя.</p>
     * example: Фёдоров Матвей
     */
    @NotBlank(groups = {CreateObject.class}, message = "При создании пользователя имя не может быть пустым.")
    private String name;

    @Email(groups = {CreateObject.class}, message = "При создании пользователя email должен быть адресом эл. почты.")
    @JsonView()
    private String email;
}

package ru.practicum.explore_with_me.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.validation.CreateObject;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * Пользователь (DTO-модель).
 */

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {
    /**
     * <p>Идентификатор.</p>
     * example: 3
     */
    private Long id;

    /**
     * <p>Имя.</p>
     * example: Фёдоров Матвей
     */
    @NotBlank(groups = {CreateObject.class}, message = "При создании пользователя имя не может быть пустым.")
    private String name;

    @Email(groups = {CreateObject.class}, message = "При создании пользователя email должен быть адресом эл. почты.")
    @NotEmpty(groups = {CreateObject.class}, message = "error with email")
    @JsonView()
    private String email;
}

package ru.practicum.explore_with_me.service.user;

import ru.practicum.explore_with_me.dto.user.UserDto;

import java.util.List;

public interface UserService {
    /**
     * Получить список всех пользователей.
     * @param from
     * @param size
     * @return список пользователей.
     */
    List<UserDto> findByIds(List<Long> ids, int from, int size);

    /**
     * Сохранить пользователя в БД.
     * @param userDto DTO-объект пользователя.
     * @return сохранённый пользователь.
     */
    UserDto save(UserDto userDto);

    /**
     * <p>Удаление пользователя.</p>
     * Delete
     * <p>/admin/users/{userId}</p>
     * @param userId ID удаляемого пользователя.
     */
    void delete(Long userId);

    /**
     * Проверка наличия пользователя в БД.
     * @param userId ID пользователя.
     * @param message сообщение для исключения.
     */
    UserDto check(Long userId, String message);
}

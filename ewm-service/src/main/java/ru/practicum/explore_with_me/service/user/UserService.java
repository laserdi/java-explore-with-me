package ru.practicum.explore_with_me.service.user;

import ru.practicum.explore_with_me.dto.user.UserShortDto;

import java.util.List;

public interface UserService {
    /**
     * Получить список всех пользователей.
     * @param from
     * @param size
     * @return список пользователей.
     */
    List<UserShortDto> findByIds(List<Long> ids, int from, int size);

    /**
     * Сохранить пользователя в БД.
     * @param userShortDto DTO-объект пользователя.
     * @return сохранённый пользователь.
     */
    UserShortDto save(UserShortDto userShortDto);

    /**
     * <p>Удаление пользователя.</p>
     * Delete
     * <p>/admin/users/{userId}</p>
     * @param userId ID удаляемого пользователя.
     */
    void delete(Long userId);

}

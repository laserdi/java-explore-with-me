package ru.practicum.explore_with_me.service.user;

import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.handler.exceptions.NotFoundRecordInBD;
import ru.practicum.explore_with_me.model.User;

import java.util.List;

public interface UserService {
    /**
     * Получить список всех пользователей.
     * @param from количество пользователей, которые нужно пропустить для формирования текущего набора.
     * @param size количество пользователей в наборе.
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
     * Вернуть DTO-пользователя из БД или сгенерировать исключение.
     * @exception NotFoundRecordInBD пользователь не найден в БД.
     * @param userId  ID пользователя.
     * @param message сообщение для исключения.
     * @return DTO найденного пользователя.
     */
    UserDto check(Long userId, String message);

    /**
     * Получение пользователя из БД.
     * @param userId  ID пользователя.
     * @param message сообщение для исключения, которое должно содержать поле с ID.
     * @return найденный пользователь.
     */
    User getUserOrThrow(Long userId, String message);
}

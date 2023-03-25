package ru.practicum.explore_with_me.service.user;

import ru.practicum.explore_with_me.model.User;

import java.util.List;

public interface UserService {
    /**
     * Получить список всех пользователей.
     * @return список пользователей.
     */
    List<User> getAll(int from, int size);

}

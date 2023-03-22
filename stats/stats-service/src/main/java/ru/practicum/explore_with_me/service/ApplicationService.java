package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.model.Application;

import java.util.Optional;

public interface ApplicationService {
    /**
     * Выдать информацию о приложении по его названию из БД.
     * @param appMame название приложения.
     * @return приложение.
     */
    Optional<Application> getByName(String appMame);

    /**
     * Сохранить новую запись о новом приложении в БД.
     * @param application приложение.
     */
    Application save(Application application);
}

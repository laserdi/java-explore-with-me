package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.StatsDtoForSave;
import ru.practicum.explore_with_me.dto.StatsDtoForView;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    /**
     * Сохранить в БД статистики информацию о запросе эндпоинта.
     * @param statDto информация об эн запросе.
     */
    void save(StatsDtoForSave statDto);

    /**
     * Получить статистику по эндпоинтам uris за период между start и end.
     * @param start  момент начала периода выдачи статистики.
     * @param end    момент конца периода выдачи статистики.
     * @param uris   список эндпоинтов.
     * @param unique выдать результаты, группируя по уникальным IP-адреса, с которых был запрос.
     * @return список статистики.
     */
    List<StatsDtoForView> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
//    Object getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}

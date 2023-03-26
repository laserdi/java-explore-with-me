package ru.practicum.explore_with_me.service.event;

import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.filter.EventFilter;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    /**
     * Поиск событий с помощью фильтра.
     * @param eventFilter фильтр.
     * @return список событий.
     */
    List<Event> findByFilter(EventFilter eventFilter);

    List<EventShortDto> getMyEvents(Long userId, Integer from, Integer size);

    /**
     * Получить события для админа.
     * @param usersId список ID юзеров.
     * @param states список статусов событий.
     * @param categoriesId список ID категорий.
     * @param rangeStart дата и время не раньше которых должно произойти событие.
     * @param rangeEnd дата и время не позже которых должно произойти событие.
     * @param from количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size количество событий в наборе.
     * @return список.
     */
    List<EventFullDto> getEventsForAdmin(List<Long> usersId, List<EventState> states, List<Long> categoriesId,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

}

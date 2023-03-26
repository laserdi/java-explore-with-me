package ru.practicum.explore_with_me.service.event;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.filter.EventFilter;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.EventState;
import ru.practicum.explore_with_me.model.QEvent;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.service.user.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserService userService;

    /**
     * Поиск событий с помощью фильтра.
     * @param eventFilter фильтр.
     * @return список событий.
     */
    @Override
    public List<Event> findByFilter(EventFilter eventFilter) {

        return null;
    }

    /**
     * <p>Получение событий, добавленных текущим пользователем.</p>
     * GET
     * /users/{userId}/events
     */
    public List<EventShortDto> getMyEvents(Long userId, Integer from, Integer size) {
        UserDto userFromDb = userService.check(userId, "Не найден пользователь с ID = {} в БД при получении" +
                " событий, добавленных текущим пользователем.");
        Pageable pageable = PageRequest.of(from, size, Sort.by("name").ascending());


        QEvent qEvent = QEvent.event;

        Predicate predicate = qEvent.initiator.id.ne(userId);
        List<EventShortDto> result = eventRepository.findAll(predicate, pageable)
                .stream().map(eventMapper::mapToDto).collect(Collectors.toList());
        log.info("Выдан результат запроса о своих событиях ({} событий), для пользователя с ID = {} и name = {}.",
                result.size(), userFromDb.getId(), userFromDb.getName());
        return result;
    }

    /**
     * Получить события для админа.
     * @param userIds      список ID юзеров.
     * @param states       список статусов событий.
     * @param categories список ID категорий.
     * @param rangeStart   дата и время не раньше которых должно произойти событие.
     * @param rangeEnd     дата и время не позже которых должно произойти событие.
     * @param from         количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size         количество событий в наборе.
     * @return список.
     */
    @Override
    public List<EventFullDto> getEventsForAdmin(List<Long> userIds, List<EventState> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                Integer from, Integer size) {

        log.info("GET /admin/events users={},states={},categories={},\nrangeStart={},rangeEnd={}" +
                ",from={},size={}", userIds, states, categories, rangeStart, rangeEnd, from, size);
        Pageable pageable = PageRequest.of(from, size, Sort.by("name").ascending());

        EventFilter eventFilter = EventFilter.builder()
                .initiator.setId(userIds).
        QEvent qEvent = QEvent.event;

        return eventRepository.getE;
    }

}

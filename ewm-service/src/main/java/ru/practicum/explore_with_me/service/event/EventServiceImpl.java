package ru.practicum.explore_with_me.service.event;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.StatsClient;
import ru.practicum.explore_with_me.dto.StatsDtoForView;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.dto.filter.EventFilter;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.handler.exceptions.NotFoundRecordInBD;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.*;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.service.category.CategoryService;
import ru.practicum.explore_with_me.service.user.UserService;
import ru.practicum.explore_with_me.util.QPredicates;
import ru.practicum.explore_with_me.util.UtilService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.explore_with_me.model.QEvent.event;

@Slf4j
@Service
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final UserService userService;
    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final StatsClient statsClient;
    private final UtilService utilService;
//    private final ParticipationRequestService participationRequestService;

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
     * GET /users/{userId}/events
     */
    public List<EventShortDto> getMyEvents(Long userId, Integer from, Integer size) {
        UserDto userFromDb = userService.check(userId, "Не найден пользователь с ID = {} в БД при получении" +
                " событий, добавленных текущим пользователем.");
        Pageable pageable = PageRequest.of(from, size, Sort.by("name").ascending());


        Predicate predicate = event.initiator.id.ne(userId);
        List<EventShortDto> result = eventRepository.findAll(predicate, pageable)
                .stream().map(eventMapper::mapToShortDto).collect(Collectors.toList());
        log.info("Выдан результат запроса о своих событиях ({} событий), для пользователя с ID = {} и name = {}.",
                result.size(), userFromDb.getId(), userFromDb.getName());
        return result;
    }

    /**
     * <p>Добавление нового события
     * POST   /users/{userId}/events
     * </p>
     * Обратите внимание: дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
     */
    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        User initiator = userService.getUserOrThrow(userId,
                "При создании события не найден пользователь ID = {}.");
        Category category = categoryService.getCatOrThrow(newEventDto.getCategoryId(),
                "При создании события не найдена категория с ID = {}.");
        Event newEvent = eventMapper.mapFromNewToModel(newEventDto);
        newEvent.setInitiator(initiator);
        newEvent.setEventState(EventState.PENDING);
        newEvent.setCreatedOn(LocalDateTime.now());
        Event savedEvent = eventRepository.save(newEvent);

        log.info("Создано событие в БД с кратким описанием: {}.", savedEvent.getAnnotation());
        return eventMapper.mapFromModelToFullDtoWhenCreate(savedEvent, 0, 0);
    }

    /**
     * Получить события для админа.
     * @param userIds    список ID юзеров.
     * @param states     список статусов событий.
     * @param categories список ID категорий.
     * @param rangeStart дата и время не раньше которых должно произойти событие.
     * @param rangeEnd   дата и время не позже которых должно произойти событие.
     * @param from       количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size       количество событий в наборе.
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
                .userIds(userIds)
//                .states(states.stream().map(Enum::toString).collect(Collectors.toList()))
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd).build();

        //Делаем предикат для статусов.
        BooleanBuilder booleanBuilderForStates = new BooleanBuilder();
        if (states != null) {
            for (EventState state : states) {
                BooleanExpression eq = event.eventState.eq(state);
                booleanBuilderForStates.andAnyOf(eq);
            }
        }
        //Делаем предикат для других фильтров кроме статусов.
        QPredicates builderWithoutStates = QPredicates.builder()
                .add(eventFilter.getUserIds(), event.initiator.id::in)
                .add(eventFilter.getCategories(), event.category.id::in)
                .add(eventFilter.getPaid(), event.paid::eq)
                .add(eventFilter.getRangeStart(), event.eventDate::after)
                .add(eventFilter.getRangeEnd(), event.eventDate::before);

        Predicate filterForAll = booleanBuilderForStates.and(builderWithoutStates.buildAnd());
        List<Event> events = eventRepository.findAll(filterForAll, pageable).toList();

        //Надо заполнить просмотры и количество подтв. запросов.
        //Получаем список просмотров от сервера статистики.
        List<StatsDtoForView> stats = new ArrayList<>();
        stats = utilService.getViews(events);
        //Заполняем поля просмотров.
//        List<Event> result = new ArrayList<>();
//        result = utilService.fillViews(events, stats);
        events = utilService.fillViews(events, stats);

        //Получаем список подтверждённых заявок для каждого события.
        Map<Event, List<ParticipationRequest>> confirmedRequests = utilService.prepareConfirmedRequest(events);
        //Заполняем количество просмотров для списка событий.
        events = utilService.fillConfirmedRequests(events, confirmedRequests);

        //Отправляем на конвертацию.
        List<EventFullDto> list = eventMapper.mapFromModelListToFullDtoList(events);
        log.info("Администратору отправлен список событий.");
        return list;
    }

    /**
     * Заполнение фильтра поиска события условиями.
     * @param userIds    список id пользователей, чьи события нужно найти.
     * @param states     список состояний в которых находятся искомые события.
     * @param categories список id категорий в которых будет вестись поиск.
     * @param rangeStart дата и время не раньше которых должно произойти событие.
     * @param rangeEnd   дата и время не позже которых должно произойти событие.
     * @return фильтр поиска события с заполненными условиями.
     */
    private EventFilter fillFilter(List<Long> userIds,
                                   List<EventState> states,
                                   List<Long> categories,
                                   LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd) {
//        List<String> list = null;
//        if (states != null) {
//            list = new ArrayList<>();
//            for (EventState state : states) {
//                String name = state.name();
//                list.add(name);
//            }
//        }
        return EventFilter.builder()
                .userIds(userIds)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
    }

//    /**
//     * Подготовить подтверждённые запросы на участие в событии.
//     * @param events список событий
//     */
//    private List<Event> prepareConfirmedRequest(List<Event> events) {
//        if (events != null && !events.isEmpty()) {
//            List<Long> eventId = new ArrayList<>();
//            for (Event ev : events) {
//                eventId.add(ev.getId());
//            } ///////////////////////////////////////////////////////
//            List<ParticipationRequestDto> result = participationRequestService.g(eventId);
//            if (result != null && !result.isEmpty()) {
//                for (RequestShort requestShort : result) {
//                    for (Event event : events) {
//                        if (requestShort.getId() == event.getId()) {
//                            event.setConfirmedRequests(requestShort.getConfirmedRequests());
//                        }
//                    }
//                }
//            }
//            events.forEach(e -> {
//                        if (e.getConfirmedRequests() == null)
//                            e.setConfirmedRequests(0L);
//                    }
//            );
//        }
//
//        for (Event ev : events) {
//            ev.setConfirmedRequests(0L);
//        }
//        return events;
//    }
//

    /**
     * Проверка наличия события в БД.
     * @param eventId ID события.
     * @param message сообщение для исключения, которое должно содержать поле с ID или быть пустым.
     */
    @Override
    public void check(Long eventId, String message) {
        if (message == null || message.isBlank()) {
            message = "В БД не найдено событие с ID = %d.";
        }
        String finalMessage = message;
        eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundRecordInBD(String.format(finalMessage, eventId)));
    }

    /**
     * Проверка наличия события в БД.
     * @param eventId ID события.
     * @param message сообщение для исключения, которое должно содержать поле с ID или быть пустым.
     * @return найденное событие.
     */
    public Event getEventOrThrow(Long eventId, String message) {
        if (message == null || message.isBlank()) {
            message = "В БД не найдено событие с ID = %d.";
        }
        String finalMessage = message;
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundRecordInBD(String.format(finalMessage, eventId)));
    }

}

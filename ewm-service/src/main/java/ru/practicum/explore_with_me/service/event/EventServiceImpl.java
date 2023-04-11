package ru.practicum.explore_with_me.service.event;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.WebClientService;
import ru.practicum.explore_with_me.dto.StatsDtoForView;
import ru.practicum.explore_with_me.dto.event.*;
import ru.practicum.explore_with_me.dto.filter.EventFilter;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.handler.exceptions.*;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.*;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.service.category.CategoryService;
import ru.practicum.explore_with_me.service.request.ParticipationRequestService;
import ru.practicum.explore_with_me.service.user.UserService;
import ru.practicum.explore_with_me.util.QPredicates;
import ru.practicum.explore_with_me.util.UtilService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.explore_with_me.model.QEvent.event;

@Slf4j
@Service
@RequiredArgsConstructor
@Import({WebClientService.class})
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final UserService userService;
    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final UtilService utilService;
    private final WebClientService webClientService;
    private final ParticipationRequestService participationRequestService;
    private static final String nameApp = "ewm-service";

    /**
     * <p>Получение событий, добавленных текущим пользователем.</p>
     * GET /users/{userId}/events
     */
    public List<EventShortDto> getMyEvents(Long userId, Integer from, Integer size) {
        UserDto userFromDb = userService.check(userId, "Не найден пользователь с ID = {} в БД при получении" +
                " событий, добавленных текущим пользователем.");
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").ascending());

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

        checkDateEvent(newEventDto.getEventDate(), 2);

        Event newEvent = eventMapper.mapFromNewToModel(newEventDto);
        newEvent.setInitiator(initiator);
        newEvent.setCategory(category);
        newEvent.setEventState(EventState.PENDING);
        newEvent.setCreatedOn(LocalDateTime.now());
        Event savedEvent = eventRepository.save(newEvent);
        EventFullDto result = eventMapper.mapFromModelToFullDtoWhenCreate(savedEvent, 0, 0);
        log.info("Создано событие в БД с ID = {} и кратким описанием: {}.", savedEvent.getId(),
                savedEvent.getAnnotation());
        return result;
    }

    /**
     * Получить события для админа.
     * @param userIds    список ID юзеров.
     * @param states     список статусов событий.
     * @param categories список ID категорий.
     * @param rangeStart дата и время не раньше которых должно произойти событие.
     * @param rangeEnd   дата и время не позже которых должно произойти событие.
     * @param text       текст для поиска в описании и аннотации.
     * @param from       количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size       количество событий в наборе.
     * @return список.
     */
    @Override
    public List<EventFullDto> getEventsForAdmin(List<Long> userIds, List<EventState> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, String text,
                                                Integer from, Integer size) {

        log.info("GET /admin/events users={},states={},categories={},\nrangeStart={},rangeEnd={}" +
                ",from={},size={}", userIds, states, categories, rangeStart, rangeEnd, from, size);
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").ascending());

        EventFilter eventFilter = EventFilter.builder()
                .userIds(userIds)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .text("%" + text.trim().toLowerCase() + "%").build();

        //Делаем предикат для статусов.
        BooleanBuilder booleanBuilderForStates = new BooleanBuilder();
        if (states != null) {
            for (EventState state : states) {
                BooleanExpression eq = event.eventState.eq(state);
                booleanBuilderForStates.andAnyOf(eq);
            }
        }
        //Делаем предикат для поиска по тексту.
        Predicate predicateForText = null;
        if (eventFilter.getText() != null && eventFilter.getText().isBlank()) {
            predicateForText = QPredicates.builder()
                    .add(event.annotation.likeIgnoreCase(eventFilter.getText()))
                    .add(event.description.likeIgnoreCase(eventFilter.getText()))
                    .buildOr();
        }

        //Делаем предикат для других фильтров кроме статусов и поиска текста.
        QPredicates qPredicatesWithoutStatesAndText = QPredicates.builder()
                .add(eventFilter.getUserIds(), event.initiator.id::in)
                .add(eventFilter.getCategories(), event.category.id::in)
                .add(eventFilter.getPaid(), event.paid::eq)
                .add(eventFilter.getRangeStart(), event.eventDate::after)
                .add(eventFilter.getRangeEnd(), event.eventDate::before);

        Predicate filterForAll = qPredicatesWithoutStatesAndText
                .add(predicateForText)
                .add(booleanBuilderForStates.getValue())
                .buildAnd();
        List<Event> events = eventRepository.findAll(filterForAll, pageable).toList();

        //Надо заполнить просмотры и количество подтв. запросов.
        //Получаем список просмотров от сервера статистики.
        List<StatsDtoForView> stats = utilService.getViews(events);
        //Заполняем поля просмотров.
        events = utilService.fillViews(events, stats);

        //Получаем список подтверждённых заявок для каждого события.
        Map<Event, List<ParticipationRequest>> confirmedRequests = utilService.prepareConfirmedRequest(events);
        //Заполняем количество просмотров для списка событий.
        events = utilService.fillConfirmedRequests(events, confirmedRequests);

        //Отправляем на конвертацию.
        List<EventFullDto> list = eventMapper.mapFromModelListToFullDtoList(events);
        log.info("Администратору отправлен список событий из {} событий.", list.size());
        return list;
    }

    /**
     * GET /events
     * <p>Получение событий с возможностью фильтрации</p>
     * <p>Обратите внимание:</p>
     * <p>это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события;</p>
     * <p>текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв;</p>
     * <p>если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события,
     * которые произойдут позже текущей даты и времени</p>
     * <p>информация о каждом событии должна включать в себя количество просмотров и количество
     * уже одобренных заявок на участие</p>
     * <p>информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики</p>
     * @param text          текстовый поиск (по аннотации и подробному описанию).
     * @param categoriesIds категории, в которых идёт поиск.
     * @param paid          показывать платные или бесплатные события.
     * @param rangeStart    дата, после которой будет начало события.
     * @param rangeEnd      дата, после до которой будет начало события.
     * @param onlyAvailable показывать только события, у которых не исчерпан лимит запросов на участие.
     * @param sort          сортировка по дате или количеству просмотров.
     * @param from          количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size          количество событий в наборе.
     * @return список событий.
     */
    @Override
    public List<EventShortDto> getEventsForAll(String text, List<Long> categoriesIds, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Boolean onlyAvailable, String sort,
                                               Integer from, Integer size,
                                               HttpServletRequest httpServletRequest) {
        log.info("Получение событий для всех с помощью фильтра. GET /events text:{},\ncategories:{}," +
                        "paid:{},rangeStart:{},rangeEnd:{},\nonlyAvailable:{},sort:{},from:{}, size:{}",
                text, categoriesIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        Sort sortForResponse;

        if (sort == null || sort.isBlank() || sort.equalsIgnoreCase("EVENT_DATE")) {
            sortForResponse = Sort.by("eventDate").ascending();
        } else if (!sort.isBlank() && sort.equals("VIEWS")) {
            sortForResponse = Sort.by("views").ascending();
        } else {
            throw new InvalidSortException(String.format("Ошибка в методе получения событий с возможностью " +
                    "фильтрации. Параметр сортировки имеет не верное значение = %s.", sort));
        }
        //Сохранили статистику на сервере.
        try {
            webClientService.saveStats(
                    nameApp,
                    httpServletRequest.getRequestURI(),
                    httpServletRequest.getRemoteAddr(),
                    LocalDateTime.now()
            );
            log.info("Sending statistics was successful");
        } catch (StatsException e) {
            log.error("Sending statistics failed");
        }

        Pageable pageable = PageRequest.of(from, size, sortForResponse);

        EventFilter eventFilter = EventFilter.builder()
                .text("%" + text.trim().toLowerCase() + "%")
                .categories(categoriesIds)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .build();
        //Делаем предикат для поиска по тексту.
        Predicate predicateForText = null;
        if (!eventFilter.getText().isBlank()) {
            predicateForText = QPredicates.builder()
                    .add(event.annotation.likeIgnoreCase(eventFilter.getText()))
                    .add(event.description.likeIgnoreCase(eventFilter.getText()))
                    .buildOr();
        }

        //Делаем предикат для других фильтров кроме статусов и поиска текста.
        QPredicates qPredicatesWithoutStatesAndText = QPredicates.builder()
                .add(eventFilter.getCategories(), event.category.id::in)
                .add(eventFilter.getPaid(), event.paid::eq)
                .add(eventFilter.getRangeStart(), event.eventDate::after)
                .add(eventFilter.getRangeEnd(), event.eventDate::before);

        Predicate filterForAll = qPredicatesWithoutStatesAndText
                .add(predicateForText)
                .buildAnd();
        List<Event> events = eventRepository.findAll(filterForAll, pageable).toList();

        //Надо заполнить просмотры и количество подтв. запросов.
        //Получаем список просмотров от сервера статистики.
        List<StatsDtoForView> stats = utilService.getViews(events);
        //Заполняем поля просмотров.
        events = utilService.fillViews(events, stats);

        //Получаем список подтверждённых заявок для каждого события.
        Map<Event, List<ParticipationRequest>> confirmedRequests = utilService.prepareConfirmedRequest(events);
        //Заполняем количество просмотров для списка событий.
        events = utilService.fillConfirmedRequests(events, confirmedRequests);

        log.info("Выдан список событий ({} шт) по запросу с фильтрами.", events.size());

        return eventMapper.mapFromModelListToShortDtoList(events);
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
                                   LocalDateTime rangeEnd,
                                   String text) {
        return EventFilter.builder()
                .userIds(userIds)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .text(text)
                .build();
    }


    /**
     * Обновить событие от имени админа.
     * <p>событие должно быть опубликовано</p>
     * <p>информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов</p>
     * <p>информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики</p>
     * <p>В случае, если события с заданным id не найдено, возвращает статус код 404</p>
     * @param eventId                 ID события.
     * @param updateEventAdminRequest объект события для обновления.
     * @return обновлённое событие.
     */
    @Override
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event eventFromDb = getEventOrThrow(eventId, "При обновлении события админом " +
                "не найдено событие в БД с ID = %d.");
        //Проверяем статус события.
        checkStateAction(eventFromDb, updateEventAdminRequest);

        eventFromDb = updateEventsFieldsByAdmin(eventFromDb, updateEventAdminRequest);        //Обновим поля события.

        eventFromDb = eventRepository.save(eventFromDb);
        //Надо заполнить просмотры и количество подтв. запросов.
        //Получаем список просмотров от сервера статистики.
        List<Event> events = List.of(eventFromDb);
        List<StatsDtoForView> stats = utilService.getViews(events);
        //Заполняем поля просмотров.
        events = utilService.fillViews(events, stats);

        //Получаем список подтверждённых заявок для каждого события.
        Map<Event, List<ParticipationRequest>> confirmedRequests = utilService.prepareConfirmedRequest(events);
        //Заполняем количество просмотров для списка событий.
        events = utilService.fillConfirmedRequests(events, confirmedRequests);
        Event result = events.get(0);
        log.info("Выполнено обновление события с ID = {}.", eventId);
        return eventMapper.mapFromModelToFullDto(result);
    }

    /**
     * <p>Получение подробной информации об опубликованном событии по его идентификатору.</p>
     * <p>Обратите внимание:</p>
     * <p>1. событие должно быть опубликовано;</p>
     * <p>2. информация о событии должна включать в себя количество просмотров и количество
     * подтвержденных запросов;</p>
     * <p>информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики.</p>
     * @param eventId            ID события.
     * @param httpServletRequest исходный запрос, отправляемый в контроллер обработки EventPublicController.
     *                           Необходим, чтобы сохранить информацию исходного запроса в БД статистики.
     */
    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest httpServletRequest) {
        log.info("Готовим ответ на запрос события по ID = {} в общедоступном режиме ", eventId);

        Event event = getEventOrThrow(eventId, "При получении события в общедоступном режиме " +
                "не найдено событие в БД с ID = %d.");

        //Надо заполнить просмотры и количество подтв. запросов.
        //Получаем список просмотров от сервера статистики.
        List<Event> events = List.of(event);
        List<StatsDtoForView> stats = utilService.getViews(events);
        //Заполняем поля просмотров.
        events = utilService.fillViews(events, stats);

        //Получаем список подтверждённых заявок для каждого события.
        Map<Event, List<ParticipationRequest>> confirmedRequests = utilService.prepareConfirmedRequest(events);
        //Заполняем количество просмотров для списка событий.
        events = utilService.fillConfirmedRequests(events, confirmedRequests);
        Event result = events.get(0);
        webClientService.saveStats("ewm-service", httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(), LocalDateTime.now());
        log.info("Отправлен ответ на запрос события по ID = {} в общедоступном режиме ", eventId);
        return eventMapper.mapFromModelToFullDto(result);
    }

    /**
     * <p>Получение подробной информации об опубликованном событии по его идентификатору.</p>
     * <p>Обратите внимание:</p>
     * <p>1. событие должно быть опубликовано;</p>
     * <p>2. информация о событии должна включать в себя количество просмотров и количество
     * подтвержденных запросов;</p>
     * <p>информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики.</p>
     * @param eventId ID события.
     * @param userId  ID пользователя.
     * @return событие.
     */
    @Override
    public EventFullDto getMyEventById(Long userId, Long eventId) {
        log.info("Готовим ответ на запрос пользователя с ID = {} события по ID = {} в приватном режиме.",
                userId, eventId);

        Event event = getEventOrThrow(eventId, "При получении пользователем с ID = "
                + userId + " собственного события с ID = %d в приватном режиме " +
                "это событие не найдено в БД .");

        //Надо заполнить просмотры и количество подтв. запросов.
        //Получаем список просмотров от сервера статистики.
        List<Event> events = List.of(event);
        List<StatsDtoForView> stats = utilService.getViews(events);
        //Заполняем поля просмотров.
        events = utilService.fillViews(events, stats);

        //Получаем список подтверждённых заявок для каждого события.
        Map<Event, List<ParticipationRequest>> confirmedRequests = utilService.prepareConfirmedRequest(events);
        //Заполняем количество просмотров для списка событий.
        events = utilService.fillConfirmedRequests(events, confirmedRequests);
        Event result = events.get(0);
        log.info("Отправлен ответ на запрос собственного события по ID = {} в приватном режиме ", eventId);
        return eventMapper.mapFromModelToFullDto(result);
    }

    /**
     * Отменить событие, созданное текущим пользователем.
     * @param userId  ID пользователя.
     * @param eventId ID события.
     */
    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundRecordInBD(String.format("При отмене события с ID = %d " +
                        "пользователем с ID = %d событие не найдено.", eventId, userId)));
        if (event.getEventState() != EventState.PENDING)
            throw new FoundConflictInDB("Отменить можно только опубликованное событие.");

        event.setEventState(EventState.CANCELED);
        event = eventRepository.save(event);
        log.info("Статус события с ID = {} изменён пользователем с ID = {} на \"Отменено - CANCELED\".",
                eventId, userId);

        //Надо заполнить просмотры и количество подтв. запросов.
        //Получаем список просмотров от сервера статистики.
        List<Event> events = List.of(event);
        List<StatsDtoForView> stats = utilService.getViews(events);
        //Заполняем поля просмотров.
        events = utilService.fillViews(events, stats);

        //Получаем список подтверждённых заявок для каждого события.
        Map<Event, List<ParticipationRequest>> confirmedRequests = utilService.prepareConfirmedRequest(events);
        //Заполняем количество просмотров для списка событий.
        events = utilService.fillConfirmedRequests(events, confirmedRequests);
        Event result = events.get(0);
        log.info("Отправлен ответ на запрос об отмене события с ID = {} пользователем с ID = {}.", eventId, userId);
        return eventMapper.mapFromModelToFullDto(result);
    }

    /**
     * Обновление события от пользователя.
     * @param userId                 ID пользователя.
     * @param eventId                ID события.
     * @param updateEventUserRequest обновляющее событие.
     * @return обновлённое событие.
     */
    @Override
    public EventFullDto updateEventUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        User userFromDb = userService.getUserOrThrow(userId,
                "При обновлении события не найден пользователь ID = {}.");
        Event eventFromDb = getEventOrThrow(eventId, "При обновлении в приватном режиме события с ID = %d " +
                "пользователем с ID = " + userId + " событие не найдено в БД.");
        Category category = null;
        //Если передана категория, то проверяем её наличие в БД.
        if (updateEventUserRequest.getCategory() != null) {
            category = categoryService.getCatOrThrow(updateEventUserRequest.getCategory(),
                    "При обновлении в приватном режиме события с ID = " + eventId
                            + "пользователем с ID = " + userId + " не найдена категория с ID = {}.");
        }

        if (!Objects.equals(userFromDb.getId(), eventFromDb.getInitiator().getId())) {
            throw new OperationFailedException("Пользователь не является инициатором события");
        }
        if (eventFromDb.getEventState().equals(EventState.PUBLISHED)) {
            throw new OperationFailedException("Невозможно обновить событие, поскольку оно уже опубликовано.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (updateEventUserRequest.getEventDate() != null) {
            checkDateEvent(updateEventUserRequest.getEventDate(), 1);
        }

        eventFromDb = updateEventsFieldsByUser(eventFromDb, updateEventUserRequest);
        Event savedEvent = eventRepository.save(eventFromDb);

        //Надо заполнить просмотры и количество подтв. запросов.
        //Получаем список просмотров от сервера статистики.
        List<Event> events = List.of(savedEvent);
        List<StatsDtoForView> stats = utilService.getViews(events);
        //Заполняем поля просмотров.
        events = utilService.fillViews(events, stats);

        //Получаем список подтверждённых заявок для каждого события.
        Map<Event, List<ParticipationRequest>> confirmedRequests = utilService.prepareConfirmedRequest(events);
        //Заполняем количество просмотров для списка событий.
        events = utilService.fillConfirmedRequests(events, confirmedRequests);
        Event result = events.get(0);
        log.info("Выполнено обновление события с ID = {}.", eventId);

        return eventMapper.mapFromModelToFullDto(result);
    }

    /**
     * GET /users/{userId}/events/{eventId}/requests
     * <p>Получение информации о запросах на участие в событии текущего пользователя.</p>
     * <p>В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список.</p>
     * @param userId  ID пользователя.
     * @param eventId ID события.
     * @return список запросов.
     */
    @Override
    public List<ParticipationRequestDto> getRequestsEvent(Long userId, Long eventId) {
        List<ParticipationRequestDto> result = new ArrayList<>();
        userService.getUserOrThrow(userId,
                "При получении запросов на участие в событиях текущего пользователя " +
                        "не найден пользователь с ID = %d.");
        getEventOrThrow(eventId, "При получении запросов на участие в событиях текущего пользователя с ID = "
                + userId + " события с ID = %d событие не найдено в БД.");
        result = participationRequestService.getRequestsForEvent(eventId);
        return result;
    }


    /**
     * Публикация события.
     * Обратите внимание:
     * <p>дата начала события должна быть не ранее, чем за час от даты публикации.</p>
     * <p>событие должно быть в состоянии ожидания публикации.</p>
     * @param eventId ID события
     * @return событие.
     */
    @Override
    public EventFullDto publish(Long eventId) {
        Event eventFromDb = getEventOrThrow(eventId, "При попытке публикации события " +
                "ID = %d оно не найдено в БД.");

        if (eventFromDb.getEventState() != EventState.PENDING)
            throw new OperationFailedException("Публиковать можно только события в статусе ожидания.");

        checkDateEvent(eventFromDb.getEventDate(), 1);

        eventFromDb.setEventState(EventState.PUBLISHED);
        eventFromDb.setPublishedOn(LocalDateTime.now());
        Event saved = eventRepository.save(eventFromDb);
        log.info("Опубликовано событие с ID = {}.", eventId);

        return eventMapper.mapFromModelToFullDto(saved);
    }


    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя.
     * <p>Обратите внимание:</p>
     * <p>если для события лимит заявок равен 0 или отключена пре-модерация заявок,
     * то подтверждение заявок не требуется;</p>
     * <p>нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное
     * событие (Ожидается код ошибки 409);</p>
     * <p>статус можно изменить только у заявок, находящихся в состоянии
     * ожидания (Ожидается код ошибки 409);</p>
     * <p>если при подтверждении данной заявки лимит заявок для события исчерпан,
     * то все неподтверждённые заявки необходимо отклонить.</p>
     * @param userId                          ID текущего пользователя.
     * @param eventId                         ID события текущего пользователя.
     * @param eventRequestStatusUpdateRequest новый статус для заявок на участие.
     */
    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        User userFromDb = userService.getUserOrThrow(userId,
                "При изменении статусов запросов не найден пользователь с ID = %d.");
        Event eventFromDb = getEventOrThrow(eventId, "При изменении статусов запросов события с ID = %d " +
                "пользователем с ID = " + userId + " событие не найдено в БД.");

        List<Event> events = setViewsAndConfirmedRequests(List.of(eventFromDb));

        eventFromDb = events.get(0);

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        if (eventFromDb.getParticipantLimit() == 0 || !eventFromDb.getRequestModeration()) {
            return result;
        }
        if (eventFromDb.getParticipantLimit() <= eventFromDb.getConfirmedRequests()) {
            throw new OperationFailedException("Закончились свободные места на участие в событии или " +
                    "запрос на участие уже был подтверждён.");
        }

        List<ParticipationRequestDto> requests =
                participationRequestService.findRequestByIds(eventRequestStatusUpdateRequest.getRequestIds());
        for (ParticipationRequestDto request : requests) {
            if (request.getStatus() != StatusRequest.PENDING) {
                throw new OperationFailedException("Статус запроса не \"в ожидании\"");
            }
            if (checkParticipantLimit(eventFromDb) &&
                    eventRequestStatusUpdateRequest.getStatus() == StatusRequest.CONFIRMED) {
                request.setStatus(StatusRequest.CONFIRMED);
                result.getConfirmedRequests().add(request);
                participationRequestService.updateRequest(request.getId(), StatusRequest.CONFIRMED);
                events = setViewsAndConfirmedRequests(events);
            } else {
                request.setStatus(StatusRequest.REJECTED);
                result.getRejectedRequests().add(request);
            }

        }
        return result;
    }


    /**
     * Если лимит больше подтверждённых заявок, то значит, есть места.
     * @param event событие.
     * @return True - места есть, False мест нет.
     */
    private boolean checkParticipantLimit(Event event) {
        if (event.getParticipantLimit() - event.getConfirmedRequests() > 0) {
            return true;
        }
        return false;
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

    /**
     * Проверить статус события перед его обновлением.
     * @param oldEvent событие.
     * @param newEvent обновляющий объект.
     */
    private void checkStateAction(Event oldEvent, UpdateEventAdminRequest newEvent) {

        if (newEvent.getStateAction() == StateAction.PUBLISH_EVENT) {
            if (oldEvent.getEventState() != EventState.PENDING) {
                throw new OperationFailedException("Невозможно опубликовать событие, поскольку его можно " +
                        "публиковать, только если оно в состоянии ожидания публикации.");
            }
        }
        if (newEvent.getStateAction() == StateAction.REJECT_EVENT) {
            if (oldEvent.getEventState() == EventState.PUBLISHED) {
                throw new OperationFailedException("Событие опубликовано, поэтому отменить его невозможно.");
            }
        }
        if (oldEvent.getEventState().equals(EventState.CANCELED)
                && newEvent.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            throw new OperationFailedException("Невозможно отменить опубликованное событие.");
        }
    }


    /**
     * Проверка даты и времени начала события.
     * @param newEventDateTime дата начала события, которую надо проверить.
     * @param plusHours        через сколько часов от текущего момента может быть начало события?
     */
    private void checkDateEvent(LocalDateTime newEventDateTime, int plusHours) {

        LocalDateTime now = LocalDateTime.now().plusHours(plusHours);
        if (now.isAfter(newEventDateTime)) {
            throw new OperationFailedException(String.format("Error 400. Field: eventDate. Error: Дата начала" +
                    " события, должна быть позже текущего момента на %s ч.", plusHours));
        }
    }

    /**
     * Метод обновления полей события.
     * @param oldEvent    обновляемое событие.
     * @param updateEvent событие с изменёнными полями.
     * @return событие.
     */
    private Event updateEventsFieldsByAdmin(Event oldEvent, UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            oldEvent.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            oldEvent.getCategory().setId(updateEvent.getCategory());
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            oldEvent.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            //Проверяем соответствие новой даты требованию, чтобы до начала события было более одного часа.
            checkDateEvent(updateEvent.getEventDate(), 1);
            oldEvent.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            oldEvent.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getPaid() != null) {
            oldEvent.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            oldEvent.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (StateAction.CANCEL_REVIEW.equals(updateEvent.getStateAction()) ||
                StateAction.REJECT_EVENT.equals(updateEvent.getStateAction())) {
            oldEvent.setEventState(EventState.CANCELED);
        }
        if (StateAction.SEND_TO_REVIEW.equals(updateEvent.getStateAction())) {
            oldEvent.setEventState(EventState.PENDING);
        }
        if (StateAction.PUBLISH_EVENT.equals(updateEvent.getStateAction())) {
            oldEvent.setEventState(EventState.PUBLISHED);
            oldEvent.setPublishedOn(LocalDateTime.now());
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            oldEvent.setTitle(updateEvent.getTitle());
        }
        return oldEvent;
    }

    /**
     * Метод обновления полей события.
     * @param oldEvent    обновляемое событие.
     * @param updateEvent событие с изменёнными полями.
     * @return событие.
     */
    private Event updateEventsFieldsByUser(Event oldEvent, UpdateEventUserRequest updateEvent) {
        if (updateEvent.getCategory() != null) {
            oldEvent.getCategory().setId(updateEvent.getCategory());
        }
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            oldEvent.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getEventDate() != null) {
            oldEvent.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            oldEvent.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            oldEvent.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getRequestModeration() != null) {
            oldEvent.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getPaid() != null) {
            oldEvent.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (StateAction.CANCEL_REVIEW.equals(updateEvent.getStateAction())) {
            oldEvent.setEventState(EventState.CANCELED);
        }
        if (StateAction.SEND_TO_REVIEW.equals(updateEvent.getStateAction())) {
            oldEvent.setEventState(EventState.PENDING);
        }
        if (StateAction.PUBLISH_EVENT.equals(updateEvent.getStateAction())) {
            oldEvent.setEventState(EventState.PUBLISHED);
            oldEvent.setPublishedOn(LocalDateTime.now());
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            oldEvent.setTitle(updateEvent.getTitle());
        }
        return oldEvent;
    }

    /**
     * Метод сохранения запросов в статистику.
     */
    private void saveStat(HttpServletRequest request) {
        try {
            webClientService.saveStats(
                    nameApp,
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    LocalDateTime.now()
            );
            log.info("Информация о запросе по этому url = {}: сохранена.", request.getRequestURI());
        } catch (StatsException e) {
            log.error("Ошибка в работе клиента статистики.");
        }
    }

    private List<Event> setViewsAndConfirmedRequests(List<Event> events) {
        //Надо заполнить просмотры и количество подтв. запросов.
        //Получаем список просмотров от сервера статистики.
        List<StatsDtoForView> stats = utilService.getViews(events);
        //Заполняем поля просмотров.
        events = utilService.fillViews(events, stats);

        //Получаем список подтверждённых заявок для каждого события.
        Map<Event, List<ParticipationRequest>> confirmedRequests = utilService.prepareConfirmedRequest(events);
        //Заполняем количество просмотров для списка событий.
        events = utilService.fillConfirmedRequests(events, confirmedRequests);
        return events;
    }

}
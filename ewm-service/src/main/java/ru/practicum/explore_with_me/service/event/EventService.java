package ru.practicum.explore_with_me.service.event;

import ru.practicum.explore_with_me.dto.event.*;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> getMyEvents(Long userId, Integer from, Integer size);

    /**
     * <p>Добавление нового события
     * POST   /users/{userId}/events
     * </p>
     * Обратите внимание: дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
     */
    EventFullDto create(Long userId, NewEventDto newEventDto);

    /**
     * Получить события для админа.
     * @param usersId      список ID юзеров.
     * @param states       список статусов событий.
     * @param categoriesId список ID категорий.
     * @param rangeStart   дата и время не раньше которых должно произойти событие.
     * @param rangeEnd     дата и время не позже которых должно произойти событие.
     * @param text         текст для поиска в описании и аннотации.
     * @param from         количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size         количество событий в наборе.
     * @return список.
     */
    List<EventFullDto> getEventsForAdmin(List<Long> usersId, List<EventState> states,
                                         List<Long> categoriesId, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, String text, Integer from, Integer size);

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
     * @param onlyAvailable показывать только доступные события.
     * @param sort          сортировка по дате или количеству просмотров.
     * @param from          количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size          количество событий в наборе.
     * @return список событий.
     */
    List<EventShortDto> getEventsForAll(String text, List<Long> categoriesIds, Boolean paid,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                        Boolean onlyAvailable, String sort,
                                        Integer from, Integer size,
                                        HttpServletRequest httpServletRequest);

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
    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    /**
     * Проверка наличия и получение события из БД.
     * @param eventId ID пользователя.
     * @param message сообщение для исключения, которое должно содержать поле с ID.
     * @return найденное событие.
     */
    Event getEventOrThrow(Long eventId, String message);

    /**
     * Обновить событие от имени админа.
     * @param eventId                 ID события.
     * @param updateEventAdminRequest объект события для обновления.
     * @return обновлённое событие.
     */
    EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    /**
     * <p>Получение подробной информации об опубликованном событии по его идентификатору.</p>
     * <p>Обратите внимание:</p>
     * <p>1. событие должно быть опубликовано;</p>
     * <p>2. информация о событии должна включать в себя количество просмотров и количество
     * подтвержденных запросов;</p>
     * <p>информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики.</p>
     */
    EventFullDto getEventById(Long eventId, HttpServletRequest httpServletRequest);

    /**
     * <p>Получение подробной информации об опубликованном событии по его идентификатору.</p>
     * <p>Обратите внимание:</p>
     * <p>1. событие должно быть опубликовано;</p>
     * <p>2. информация о событии должна включать в себя количество просмотров и количество
     * подтвержденных запросов;</p>
     * <p>информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики.</p>
     * @param userId  ID пользователя.
     * @param eventId ID события.
     * @return событие.
     */
    EventFullDto getMyEventById(Long eventId, Long userId);

    /**
     * Отменить событие, созданное текущим пользователем.
     * @param userId  ID пользователя.
     * @param eventId ID события.
     */
    EventFullDto cancelEvent(Long userId, Long eventId);

    /**
     * Обновление события от пользователя.
     * @param userId                 ID пользователя.
     * @param updateEventUserRequest обновляющее событие.
     * @param eventId                ID события.
     * @return обновлённое событие.
     */
    EventFullDto updateEventUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    /**
     * GET /users/{userId}/events/{eventId}/requests
     * <p>Получение информации о запросах на участие в событии текущего пользователя.</p>
     * <p>В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список.</p>
     * @param userId  ID пользователя.
     * @param eventId ID события.
     * @return список запросов.
     */
    List<ParticipationRequestDto> getRequestsEvent(Long userId, Long eventId);


    /**
     * Публикация события.
     * Обратите внимание:
     * <p>дата начала события должна быть не ранее, чем за час от даты публикации.</p>
     * <p>событие должно быть в состоянии ожидания публикации.</p>
     * @param eventId ID события.
     */
    EventFullDto publish(Long eventId);


    }

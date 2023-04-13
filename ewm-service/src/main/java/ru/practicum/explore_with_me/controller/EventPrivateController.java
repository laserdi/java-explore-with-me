package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventUserRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.service.event.EventService;
import ru.practicum.explore_with_me.validation.CreateObject;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventPrivateController {
    private final EventService eventService;

    /**
     * <p>Получение событий, добавленных текущим пользователем.</p>
     * GET /users/{userId}/events
     * <p>В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список</p>
     * @param userId ID пользователя.
     * @param from   количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size   количество событий в наборе.
     * @return список событий пользователя.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getMyEvents(@PathVariable @Positive Long userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение событий, добавленных текущим пользователем с ID = {}.", userId);
        return eventService.getMyEvents(userId, from, size);
    }

    /**
     * Добавление нового события.
     * <p>POST /users/{userId}/events</p>
     * Обратите внимание: дата и время на которые намечено событие не может быть раньше,
     * чем через два часа от текущего момента.
     * @param userId      ID пользователя.
     * @param newEventDto добавляемое событие.
     * @return сохранённое событие.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable("userId") @Positive Long userId,
                                 @Validated(CreateObject.class) @RequestBody NewEventDto newEventDto) {

        log.info("Добавление нового события. POST /users/userId/events userId={}, newEvent = {}.", userId, newEventDto);
        return eventService.create(userId, newEventDto);
    }

    /**
     * <p>Получение полной информации о событии, добавленном текущим пользователем.</p>
     * GET /users/{userId}/events/{eventId}
     * <p>В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список</p>
     * @param userId ID события.
     * @return событие пользователя.
     */
    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable("userId") @Positive Long userId,
                                     @PathVariable("eventId") @Positive Long eventId) {
        log.info("Получение полной информации о событии, добавленном текущим пользователем.\n" +
                "GET /users/{userId}/events/{eventId}: userId = {}, eventId = {}", userId, eventId);

        return eventService.getMyEventById(userId, eventId);
    }

    /**
     * Изменение события добавленного текущим пользователем
     * <p>PATCH /users/{userId}/events</p>
     * @param userId                 ID пользователя.
     * @param updateEventUserRequest обновляющее событие.
     * @return обновлённое событие.
     */


    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("Обновление события от пользователя. Patch /users/{}/events/ updateEvent = {}.",
                userId, updateEventUserRequest);
        return eventService.updateEventUser(userId, eventId, updateEventUserRequest);
    }

    /**
     * <p>Получение информации о запросах на участие в событии текущего пользователя.</p>
     * GET /users/{userId}/events/{eventId}/requests
     * <p>В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список.</p>
     * @param userId  ID пользователя.
     * @param eventId ID события.
     * @return список запросов.
     */
    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsOld(@PathVariable @Positive Long userId,
                                                        @PathVariable @Positive Long eventId) {
        log.info("GET /users/{userId}/events//{eventId}/requests userId {}, eventId {}", userId, eventId);
        return eventService.getRequestsEvent(userId, eventId);
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя.
     * <p>PATCH /users/{userId}/events/{eventId}/requests</p>
     * <p>Обратите внимание:</p>
     * <p>если для события лимит заявок равен 0 или отключена пре-модерация заявок,
     * то подтверждение заявок не требуется;</p>
     * <p>нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное
     * событие (Ожидается код ошибки 409);</p>
     * <p>статус можно изменить только у заявок, находящихся в состоянии
     * ожидания (Ожидается код ошибки 409);</p>
     * <p>если при подтверждении данной заявки лимит заявок для события исчерпан,
     * то все неподтверждённые заявки необходимо отклонить.</p>
     */
    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestsStatus(@Positive @PathVariable Long userId,
                                                               @Positive @PathVariable Long eventId,
                                                               @Valid @RequestBody EventRequestStatusUpdateRequest
                                                                       eventRequestStatusUpdateRequest) {

        log.info("Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя.\n" +
                "PATCH /users/{userId}/events//{eventId}/requests userId = {}, eventId = {}," +
                " Тело запроса: = {}", userId, eventId, eventRequestStatusUpdateRequest);

        return eventService.updateRequestStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }

}
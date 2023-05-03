package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventAdminRequest;
import ru.practicum.explore_with_me.model.EventState;
import ru.practicum.explore_with_me.service.event.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventAdminController {
    private final EventService eventService;

    /**
     * Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия.
     * @param users      список id пользователей, чьи события нужно найти.
     * @param states     список состояний в которых находятся искомые события.
     * @param categories список id категорий в которых будет вестись поиск.
     * @param rangeStart дата и время не раньше которых должно произойти событие.
     * @param rangeEnd   дата и время не позже которых должно произойти событие.
     * @param text       текст поиска в аннотации и описании.
     * @param from       количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size       количество событий в наборе.
     * @return список событий.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsForAdmin(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<EventState> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) LocalDateTime rangeStart,
                                                @RequestParam(required = false) LocalDateTime rangeEnd,
                                                @RequestParam(required = false, defaultValue = "") String text,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET /admin/events users={},states={},categories={},\nrangeStart={},rangeEnd={}" +
                ",from={},size={}", users, states, categories, rangeStart, rangeEnd, from, size);

        return eventService.getEventsForAdmin(users, states, categories,
                rangeStart, rangeEnd, text, from, size);
    }

    /**
     * Обновление события.
     * @param eventId                 ID события.
     * @param updateEventAdminRequest входящие параметры для обновления.
     * @return обновлённое событие.
     */
    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PositiveOrZero @PathVariable Long eventId,
                               @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Обновление события администратором.\t\tPATCH /admin/events event id{}, {}", eventId,
                updateEventAdminRequest);
        return eventService.updateEventAdmin(eventId, updateEventAdminRequest);
    }

    /**
     * Публикация события.
     * <p>PATCH /admin/events/{eventId}/publish</p>
     * Обратите внимание:
     * <p>дата начала события должна быть не ранее, чем за час от даты публикации.</p>
     * <p>событие должно быть в состоянии ожидания публикации.</p>
     * @param eventId ID публикуемого события.
     */
    @PatchMapping("/{eventId}/publish")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto publishEvent(@Positive @PathVariable Long eventId) {
        log.info("Публикация события с ID = {}", eventId);
        return eventService.publish(eventId);
    }
}

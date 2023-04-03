package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventPrivateController {
    private final EventService eventService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @Valid @RequestBody NewEventDto newEventDto) {

        log.info("POST /users/userId/events userId={}, {}", userId, newEventDto);
        return eventService.create(userId, newEventDto);
    }

    /**
     * GET /users/{userId}/events
     * <p>Получение событий, добавленных текущим пользователем.</p>
     * <p>В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список</p>
     * @param userId ID пользователя.
     * @param from   количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size   количество событий в наборе.
     * @return список событий пользователя.
     */
//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<EventShortDto> getEvents(@PathVariable Long userId,
//                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
//                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
//        log.info("GET /users/{userId}/events - userId {}, from {}, size {}", userId, from, size);
//        return eventService.getMyEvents(userId, from, size);
//    }

    /**
     * GET /users/{userId}/events
     * <p>Получение событий, добавленных текущим пользователем.</p>
     * <p>В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список</p>
     * @param userId ID пользователя.
     * @param from   количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size   количество событий в наборе.
     * @return список событий пользователя.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getMyEvents(@PathVariable @PositiveOrZero Long userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @PositiveOrZero @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение событий, добавленных текущим пользователем с ID = {}.", userId);
        return eventService.getMyEvents(userId, from, size);
    }


}
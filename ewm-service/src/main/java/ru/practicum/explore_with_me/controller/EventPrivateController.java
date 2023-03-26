package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.service.event.EventService;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET /users/{userId}/events userId {}, from {}, size {}", userId, from, size);
        return eventService.getEventsForPrivate(userId, from, size);
    }

    /**
     * <p>Получение событий, добавленных текущим пользователем.</p>
     * GET
     * <p>/users/{userId}/events</p>
     */
    public List<EventShortDto> getMyEvents(@PathVariable @PositiveOrZero Long userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @PositiveOrZero @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение событий, добавленных текущим пользователем с ID = {}.", userId);
        return eventService.getMyEvents(userId, from, size);
    }
}

package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventPublicController {
    private final EventService eventService;

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
     * @param text               текстовый поиск (по аннотации и подробному описанию).
     * @param categories         категории, в которых идёт поиск.
     * @param paid               показывать платные или бесплатные события.
     * @param rangeStart         дата, после которой будет начало события.
     * @param rangeEnd           дата, после до которой будет начало события.
     * @param onlyAvailable      показывать только доступные события.
     * @param sort               сортировка по дате или количеству просмотров.
     * @param from               количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size               количество событий в наборе.
     * @param httpServletRequest адрес запроса.
     * @return список событий.
     */
    @GetMapping
    public List<EventShortDto> findByFilter(HttpServletRequest httpServletRequest,
                                            @RequestParam(name = "text", defaultValue = "") String text,
                                            @RequestParam(name = "categories", required = false) List<Long> categories,
                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false)
                                            LocalDateTime rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false)
                                            LocalDateTime rangeEnd,
                                            @RequestParam(name = "onlyAvailable", defaultValue = "false")
                                            boolean onlyAvailable,
                                            @RequestParam(name = "sort", defaultValue = "EVENT_DATE") String sort,
                                            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return eventService.getEventsForAll(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, httpServletRequest);
    }

    /**
     * GET /events/{id}
     * <p>Получение подробной информации об опубликованном событии по его идентификатору.</p>
     * <p>Обратите внимание:</p>
     * <p>1. событие должно быть опубликовано;</p>
     * <p>2. информация о событии должна включать в себя количество просмотров и количество
     * подтверждённых запросов;</p>
     * <p>информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики.</p>
     */
    @GetMapping("/{id}")
    public EventFullDto findEventById(HttpServletRequest httpServletRequest,
                                     @PathVariable("id") @Positive Long eventId) {
        return eventService.getEventById(eventId, httpServletRequest);
    }

}

package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.StatsDtoForSave;
import ru.practicum.explore_with_me.dto.StatsDtoForView;
import ru.practicum.explore_with_me.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    /**
     * Получить статистику.
     */
    @GetMapping("/stats")
    public List<StatsDtoForView> getStats(@RequestParam("start") LocalDateTime start,
                                          @RequestParam("end") LocalDateTime end,
                                          @RequestParam("uris") List<String> uris,
                                          @RequestParam("unique") boolean unique) {
        log.info("Получение статистики за период с {} по {} по эндпоинтам ({}). unique = {}.", start, end,
                uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@Valid @RequestBody StatsDtoForSave statsDtoForSave) {
        log.info("Сохранение статистики {}.", statsDtoForSave);
        statsService.save(statsDtoForSave);
    }
}

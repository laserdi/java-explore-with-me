package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.StatsDtoForSave;
import ru.practicum.explore_with_me.dto.StatsDtoForView;
import ru.practicum.explore_with_me.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @Value("${svc.date.time.formatter}")
    private String dateTimeFormatterPattern;

    /**
     * Получить статистику.
     */
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatsDtoForView> getStats(@RequestParam("start") String startS,
//    public Object getStats(@RequestParam("start") String startS,
                                          @RequestParam("end") String endS,
                                          @RequestParam(value = "uris", required = false) List<String> uris,
                                          @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        log.info("Получение статистики за период с {} по {} по эндпоинтам ({}). unique = {}.", startS, endS,
                uris, unique);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatterPattern);
        LocalDateTime start = LocalDateTime.parse(startS, formatter);
        LocalDateTime end = LocalDateTime.parse(endS, formatter);
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void add(@Valid @RequestBody StatsDtoForSave statsDtoForSave) {
        log.info("Сохранение статистики {}.", statsDtoForSave);

        statsService.save(statsDtoForSave);
    }
}
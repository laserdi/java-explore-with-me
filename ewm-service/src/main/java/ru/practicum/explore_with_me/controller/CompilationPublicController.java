package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.service.compilation.CompilationService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {

    private final CompilationService compilationService;

    /**
     * <p>Получение подборок событий.</p>
     * GET /compilations
     * @param pinned искать только закрепленные/не закрепленные подборки.
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора.
     * @param size   количество элементов в наборе. Default value : 10
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(@RequestParam(required = false) boolean pinned,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение подборок событий. GET /compilations pinned = {}, from = {}, size = {}.", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

    /**
     * GET /compilations/{compId}
     * <p>Получение подборки событий по его id.</p>
     * @param compId ID подборки.
     * @return подборка событий.
     */
    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationById(@NotNull @PositiveOrZero @PathVariable Long compId) {
        log.info("Получение подборки событий по его id. GET /compilations compId {}", compId);
        return compilationService.getCompilationById(compId);
    }
}

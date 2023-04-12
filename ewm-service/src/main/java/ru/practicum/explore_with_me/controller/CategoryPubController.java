package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.category.CategoryDto;
import ru.practicum.explore_with_me.service.category.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
@Validated
public class CategoryPubController {
    private final CategoryService categoryService;

    /**
     * <p>Получение категорий.</p>
     * GET /categories
     * @param catId ID категории.
     * @return категория.
     */
    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategory(@Positive @PathVariable Long catId) {
        log.info("GET categories/ catId= {}", catId);
        return categoryService.getById(catId);
    }

    /**
     * В случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список.
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора.
     * @param size количество категорий в наборе.
     * @return список категорий.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAllCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрошены все категории. GET /categoriesAll/?from={}?size={}", from, size);
        return categoryService.getAll(from, size);
    }
}

package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.category.CategoryDto;
import ru.practicum.explore_with_me.service.category.CategoryService;
import ru.practicum.explore_with_me.validation.CreateObject;
import ru.practicum.explore_with_me.validation.UpdateObject;

import javax.validation.constraints.PositiveOrZero;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    /**
     * Сохранение новой категории в БД.
     * @return созданная категория.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Validated(CreateObject.class) @RequestBody CategoryDto categoryDto) {
        log.info("Сохранение категории в БД. POST /admin/categories\t\t{}", categoryDto);
        return categoryService.save(categoryDto);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PositiveOrZero @PathVariable Long catId,
                                              @Validated(UpdateObject.class) @RequestBody CategoryDto categoryDto) {
        log.info("Обновление категории в БД. PATCH /admin/categories/catId={}, categoryDto={}", catId, categoryDto);
        return categoryService.update(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PositiveOrZero @PathVariable Long catId) {
        log.info("DELETE/admin/categories/ catId={}", catId);
        categoryService.delete(catId);
    }

}

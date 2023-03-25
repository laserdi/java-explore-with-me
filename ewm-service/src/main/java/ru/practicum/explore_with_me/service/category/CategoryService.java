package ru.practicum.explore_with_me.service.category;

import ru.practicum.explore_with_me.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {
    ///admin/categories

    /**
     * Получить список всех категорий.
     * @return список категорий.
     */
    List<CategoryDto> getAll(int from, int size);

    /**
     * Получить категорию по ID.
     * @param id ID категории.
     * @return категория.
     */
    CategoryDto getById(Long id);

    /**
     * Добавить в БД новую категорию.
     * @param categoryDto DTO-категория.
     * @return добавленный объект DTO-категории.
     */
    CategoryDto add(CategoryDto categoryDto);

    /**
     * Обновить категорию в БД.
     * @return изменённая категория.
     */
    CategoryDto update(CategoryDto categoryDto);
}

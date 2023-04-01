package ru.practicum.explore_with_me.service.category;

import ru.practicum.explore_with_me.dto.category.CategoryDto;
import ru.practicum.explore_with_me.model.Category;

import java.util.List;

public interface CategoryService {
    ///admin/categories

    /**
     * Получить список всех категорий.
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора.
     * @param size количество категорий в наборе.
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
    CategoryDto save(CategoryDto categoryDto);

    /**
     * Обновить категорию в БД.
     * @return изменённая категория.
     */
    CategoryDto update(Long id, CategoryDto categoryDto);

    /**
     * Удалить категорию по ID.
     * @param catId ID удаляемой категории.
     * @return удалённая категория.
     */
    void delete(Long catId);

    /**
     * <p>Получение категории из БД по ID.</p>
     * @param catId ID категории.
     * @param message сообщение об ошибке.
     * @return найденная категория.
     */
    Category getCatOrThrow(Long catId,String message);
}

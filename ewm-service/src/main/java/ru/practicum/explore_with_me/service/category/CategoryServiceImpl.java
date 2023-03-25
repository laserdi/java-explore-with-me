package ru.practicum.explore_with_me.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.apierror.exceptions.NotFoundRecordInBD;
import ru.practicum.explore_with_me.dto.category.CategoryDto;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Получить список всех категорий.
     * @return список категорий.
     */
    @Override
    public List<CategoryDto> getAll(int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("name").ascending());
        List<Category> categories = categoryRepository.findAll(pageable).getContent();
        return categories.stream()
                .map(categoryMapper::mapToCategoryDto).collect(Collectors.toList());
    }

    /**
     * Получить категорию по ID.
     * @param id ID категории.
     * @return категория.
     */
    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundRecordInBD(
                        String.format("В БД не найдена категория с ID = %1$d", id)));
        return categoryMapper.mapToCategoryDto(category);
    }

    /**
     * Добавить в БД новую категорию.
     * @param categoryDto DTO-категория.
     * @return добавленный объект DTO-категории.
     */
    @Override
    public CategoryDto add(CategoryDto categoryDto) {
        Category newCategory = categoryMapper.mapToCategory(categoryDto);
        return categoryMapper.mapToCategoryDto(categoryRepository.save(newCategory));
    }

    /**
     * Обновить категорию в БД.
     * @param categoryDto DTO-категория.
     * @return изменённая категория.
     */
    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        categoryRepository.findById(categoryDto.getId()).orElseThrow(() -> new NotFoundRecordInBD(
                String.format("При обновлении категории в БД не найдена категория с ID = %1$s", categoryDto))
        );
        Category newCategory = categoryMapper.mapToCategory(categoryDto);
        return categoryMapper.mapToCategoryDto(categoryRepository.save(newCategory));
    }
}

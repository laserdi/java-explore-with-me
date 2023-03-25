package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.dto.category.CategoryDto;
import ru.practicum.explore_with_me.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category mapToCategory(CategoryDto categoryDto);

    CategoryDto mapToCategoryDto(Category category);
}

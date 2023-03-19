package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.dto.StatDto;
import ru.practicum.explore_with_me.dto.StatsDtoForSave;
import ru.practicum.explore_with_me.dto.StatsDtoForView;
import ru.practicum.explore_with_me.model.Stat;

@Mapper(componentModel = "spring")
public interface StatMapper {
    @Mapping(source = "app", target = "app.app")
    Stat mapFromSaveToModel(StatsDtoForSave statsDtoForSave);

    @Mapping(source = "app.app", target = "app")
    StatsDtoForSave mapToDtoForSave(Stat stat);

    @Mapping(source = "app", target = "app.app")
    StatDto mapFromViewToStatDto(StatsDtoForView statsDtoForView);

    @Mapping(source = "app.app", target = "app")
    StatsDtoForView mapToDtoForView(StatDto statDto);
}

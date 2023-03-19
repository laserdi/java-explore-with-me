package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.dto.StatDto;
import ru.practicum.explore_with_me.dto.StatsDtoForSave;
import ru.practicum.explore_with_me.dto.StatsDtoForView;
import ru.practicum.explore_with_me.model.Stat;

@Mapper(componentModel = "spring")
public interface StatMapper {
    Stat mapFromSaveToModel(StatsDtoForSave statsDtoForSave);

    StatsDtoForSave mapToDtoForSave(Stat stat);

    StatDto mapFromViewToStatDto(StatsDtoForView statsDtoForView);

    StatsDtoForView mapToDtoForView(StatDto statDto);

}

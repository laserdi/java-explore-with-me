package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.dto.event.LocationDto;
import ru.practicum.explore_with_me.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location mapToModel(LocationDto locationDto);
}

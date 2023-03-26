package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.model.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event mapToModel(EventShortDto eventShortDto);

    EventShortDto mapToDto(Event event);
}

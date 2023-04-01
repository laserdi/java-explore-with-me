package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.model.ParticipationRequest;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {
    @Mapping(target = "event.id", source = "event")
    @Mapping(target = "requester.id", source = "requester")
    ParticipationRequest mapToModel(ParticipationRequestDto participationRequestDto);

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto mapToDto(ParticipationRequest participationRequest);
}

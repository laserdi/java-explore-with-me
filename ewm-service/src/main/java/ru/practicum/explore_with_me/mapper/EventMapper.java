package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.dto.event.*;
import ru.practicum.explore_with_me.model.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event mapFromShortToModel(EventShortDto eventShortDto);

    EventShortDto mapToShortDto(Event event);

    @Mapping(target = "paid", source = "paid", defaultValue = "false")
    @Mapping(target = "participantLimit", source = "participantLimit", defaultValue = "0")
    @Mapping(target = "requestModeration", source = "requestModeration", defaultValue = "true")
    Event mapFromNewToModel(NewEventDto newEventDto);

    NewEventDtoForResponse mapToNewDtoForResponse(Event event);

    @Mapping(source = "integer", target = "views")
    @Mapping(source = "confRequests", target = "confirmedRequests")
    EventFullDto mapFromModelToFullDtoWhenCreate(Event event, int confRequests, int integer);

    EventFullDto mapFromModelToFullDto(Event event);

    //    default EventFullDto mapFromModelToFullDto(Event event);
    @Mapping(target = "views", source = "viewsForMapper.viewsForMapper")
    @Mapping(source = "viewsForMapper.confirmedRequestsForMapper", target = "confirmedRequests")
    EventFullDto mapFromModelToFullDtoQ(Event event, ViewsForMapper viewsForMapper);

    List<EventFullDto> mapFromModelListToFullDtoList(List<Event> eventList);

    List<EventShortDto> mapFromModelListToShortDtoList(List<Event> eventList);

}
package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.dto.event.EventFullDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.dto.event.ViewsForMapper;
import ru.practicum.explore_with_me.model.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event mapFromShortToModel(EventShortDto eventShortDto);

    EventShortDto mapToShortDto(Event event);

    Event mapFromNewToModel(NewEventDto newEventDto);

    //
//    @Mapping(source = "id", ignore = true)
//    @Mapping(target = "paid", source = "event.paid")
//    @Mapping(target = "annotation", source = "event.annotation")
//    @Mapping(target = "eventDate", source = "event.eventDate")
//    NewEventDto mapToNewDto(Event event, EventShortDto eventShortDto);


    @Mapping(source = "integer", target = "views")
    @Mapping(source = "confRequests", target = "confirmedRequests")
    EventFullDto mapFromModelToFullDtoWhenCreate(Event event, int confRequests, int integer);

    //    default EventFullDto mapFromModelToFullDto(Event event);
    @Mapping(target = "views", source = "viewsForMapper.viewsForMapper")
    @Mapping(source = "viewsForMapper.confirmedRequestsForMapper", target = "confirmedRequests")
    EventFullDto mapFromModelToFullDtoQ(Event event, ViewsForMapper viewsForMapper);

    List<EventFullDto> mapFromModelListToFullDtoList(List<Event> eventList);
}

package ru.practicum.explore_with_me.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explore_with_me.dto.category.CategoryDto;
import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.user.UserShortDto;
import ru.practicum.explore_with_me.model.Compilation;
import ru.practicum.explore_with_me.model.Event;

import java.util.ArrayList;
import java.util.List;

//приватный конструктор без параметров, тем самым запретив создание объектов этого класса)
// Так это не имеет смысла ввиду того, что все в этом классе static, а значит класс не имеет состояния)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomMapper {
    public static CompilationDto mapFromNewDtoToModel(Compilation compilation) {
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        for (Event event : compilation.getEvents()) {
            eventShortDtoList.add(EventShortDto.builder()
                    .annotation(event.getAnnotation())
                    .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                    .confirmedRequests(event.getConfirmedRequests())
                    .eventDate(event.getEventDate())
                    .id(event.getId())
                    .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                    .paid(event.getPaid())
                    .title(event.getTitle())
                    .views(event.getViews())
                    .build());
        }

        CompilationDto compilationDto = CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(eventShortDtoList)
                .build();
        return compilationDto;
    }
}

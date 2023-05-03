package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.dto.compilation.NewCompilationDto;
import ru.practicum.explore_with_me.model.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    @Mapping(ignore = true, target = "events")
    Compilation mapFromDtoToModel(CompilationDto compilationDto);

    @Mapping(ignore = true, target = "events", source = "events")
    CompilationDto mapToDto(Compilation compilation);

    @Mapping(ignore = true, target = "events", source = "events")
    Compilation mapFromNewDtoToModel(NewCompilationDto newCompilationDto);
}

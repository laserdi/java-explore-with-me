package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.explore_with_me.dto.comment.CommentForView;
import ru.practicum.explore_with_me.dto.comment.CommentUserDto;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "event", source = "eventMap")
    @Mapping(target = "user", source = "userMap")
    Comment mapToModelFromDto(CommentUserDto commentUserDto, Event eventMap, User userMap);

    CommentUserDto mapToComUserDto(Comment comment);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "userId", source = "user.id")
    CommentForView mapToView(Comment comment);

    List<CommentForView> mapFromModelLisToViewList(List<Comment> comments);
}

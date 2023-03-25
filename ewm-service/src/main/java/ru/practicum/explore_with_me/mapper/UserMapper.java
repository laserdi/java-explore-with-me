package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.dto.user.UserShortDto;
import ru.practicum.explore_with_me.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapToUser(UserShortDto userShortDto);

    UserShortDto mapToUserDto(User user);
}

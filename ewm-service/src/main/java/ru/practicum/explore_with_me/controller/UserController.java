package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore_with_me.dto.user.UserShortDto;
import ru.practicum.explore_with_me.mapper.UserMapper;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.service.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController("/admin/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * GET
     * /admin/users
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserShortDto> findAll(int from, int size) {
        List<User> users = userService.getAll(from, size);
        return users.stream().map(userMapper::mapToUserDto).collect(Collectors.toList());
    }

}

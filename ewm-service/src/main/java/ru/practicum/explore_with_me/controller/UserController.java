package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.service.user.UserService;
import ru.practicum.explore_with_me.validation.CreateObject;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;

@Validated
@RestController("/admin/users")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;


    /**
     * Возвращает информацию обо всех пользователях (учитываются параметры ограничения выборки),
     * либо о конкретных (учитываются указанные идентификаторы).
     * <p>В случае, если по заданным фильтрам не найдено ни одного пользователя, возвращает пустой список.</p>
     * GET
     * /admin/users
     * @param ids ID требуемых пользователей.
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора.
     * @param size   количество элементов в наборе. Default value : 10
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findAll(@RequestParam(required = false) List<Long> ids,
                                 @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                 @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /admin/users. ids={}, from={}, size={}", ids, from, size);
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return userService.findByIds(ids, from, size);
    }

    /**
     * <p>Добавление нового пользователя.</p>
     * POST
     * <p>/admin/users</p>
     * @param userDto входящие параметры.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Validated(CreateObject.class) @RequestBody UserDto userDto) {
        log.info("Добавление нового пользователя. POST /admin/users Body user = {}", userDto);
        return userService.save(userDto);
    }

    /**
     * <p>Удаление пользователя.</p>
     * DELETE
     * <p>/admin/users/{userId}</p>
     * @param userId ID пользователя.
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long userId) {
        log.info("Удаление пользователя с ID = {}.", userId);
        userService.delete(userId);
    }
}

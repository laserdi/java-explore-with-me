package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.user.UserShortDto;
import ru.practicum.explore_with_me.service.user.UserService;
import ru.practicum.explore_with_me.validation.CreateObject;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

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
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserShortDto> findAll(@RequestParam List<Long> ids,
                                      @Positive @RequestParam(required = false, defaultValue = "0") int from,
                                      @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("GET /admin/users. ids={}, from={}, size={}", ids, from, size);
        return userService.findByIds(ids, from, size);
    }

    /**
     * <p>Добавление нового пользователя.</p>
     * POST
     * <p>/admin/users</p>
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserShortDto addUser(@Validated(CreateObject.class) @RequestBody UserShortDto userShortDto) {
        log.info("Добавление нового пользователя. POST /admin/users Body user = {}", userShortDto);
        return userService.save(userShortDto);
    }

    /**
     * <p>Удаление пользователя.</p>
     * DELETE
     * <p>/admin/users/{userId}</p>
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @PositiveOrZero Long userId) {
        log.info("Удаление пользователя с ID = {}.", userId);
        userService.delete(userId);
    }
}

package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.comment.CommentForView;
import ru.practicum.explore_with_me.dto.comment.CommentUserDto;
import ru.practicum.explore_with_me.service.comment.CommentService;
import ru.practicum.explore_with_me.validation.CreateObject;
import ru.practicum.explore_with_me.validation.UpdateObject;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
@Validated
public class CommentUserController {
    private final CommentService commentService;

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentForView addCommentByUser(@PathVariable("userId") @Positive @NotNull Long userId,
                                           @Validated(CreateObject.class) @RequestBody CommentUserDto commentUserDto) {
        log.info("Создание комментария пользователем с ID = {} к событию с ID = {}.\t" +
                        "Post /comments/user/{}",
                userId, commentUserDto.getEventId(), userId);
        return commentService.addComment(userId, commentUserDto);
    }

    @GetMapping("/{comId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentForView getByIdForUser(@PathVariable("userId") @Positive Long userId,
                                         @PathVariable("comId") @Positive Long commId) {
        log.info("Получение комментария с ID  = {} пользователем с ID = {}.\t" +
                "Get /comments/{}/user/{}", commId, userId, commId, userId);
        return commentService.getCommentById(userId, commId);
    }

    /**
     * Обновление комментария.
     * @param comId            ID комментария.
     * @param userId           ID пользователя, обновляющего комментарий.
     * @param updateCommentDto обновляемый комментарий.
     * @return обновлённый комментарий.
     */
    @PatchMapping("/{comId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentForView updateComment(@PathVariable @NotNull @PositiveOrZero Long comId,
                                        @PathVariable @NotNull @PositiveOrZero Long userId,
                                        @RequestBody @Validated(UpdateObject.class) CommentUserDto updateCommentDto) {
        log.info("Обновление комментария с ID = {} пользователем с ID = {}.\t" +
                " Patch /comments/{}/user/{}/ ", comId, userId, comId, userId);
        return commentService.updateComment(comId, userId, updateCommentDto);
    }

    @DeleteMapping("/{comId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByUser(@PathVariable @NotNull @PositiveOrZero Long comId,
                             @PathVariable @NotNull @PositiveOrZero Long userId) {
        log.info("Удаление комментария с ID = {} пользователем с ID = {}.", userId, comId);
        commentService.deleteCommentByUser(comId, userId);
    }

    /**
     * Получение списка заявок к событию с ID = {}.
     */
    @GetMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentForView> getAllCommentEvent(@PathVariable @PositiveOrZero @NotNull Long eventId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получение списка комментариев к событию с ID = {}\t" +
                "GET /comments/event/{}from={}size={}", eventId, eventId, from, size);
        return commentService.getCommentsForEvent(eventId, from, size);
    }
}

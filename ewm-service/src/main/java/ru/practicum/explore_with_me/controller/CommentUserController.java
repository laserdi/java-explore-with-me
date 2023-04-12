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

import javax.validation.constraints.Positive;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/{userId}/comments/")
@Validated
public class CommentUserController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentForView addCommentByUser(@PathVariable("userId") @Positive Long userId,
                                           @Validated(CreateObject.class) @RequestBody CommentUserDto commentUserDto) {
        log.info("Создание комментария пользователем с ID = {} к событию с ID = {}.",
                userId, commentUserDto.getEventId());
        return commentService.addComment(userId, commentUserDto);
    }

    @GetMapping("/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentForView getByIdForUser(@PathVariable("userId") @Positive Long userId,
                                         @PathVariable("comId") @Positive Long commId) {
        log.info("Получение комментария с ID  = {} пользователем с ID = {}.", commId, userId);
        return commentService.getCommentById(userId, commId);
    }
}

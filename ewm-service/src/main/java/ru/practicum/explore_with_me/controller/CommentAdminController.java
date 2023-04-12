package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.service.comment.CommentService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/comments/")
@Validated
public class CommentAdminController {
    private final CommentService commentService;

    /**
     * Удаление комментария администратором.
     * @param comId ID комментария.
     */
    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByAdmin(@PathVariable @NotNull @PositiveOrZero Long comId) {
        log.info("Удаление комментария с ID = {} администратором. " +
                "DELETE /admin/comments/{}", comId, comId);
        commentService.deleteCommentByAdmin(comId);
    }
}

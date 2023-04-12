package ru.practicum.explore_with_me.service.comment;

import ru.practicum.explore_with_me.dto.comment.CommentForView;
import ru.practicum.explore_with_me.dto.comment.CommentUserDto;
import ru.practicum.explore_with_me.handler.exceptions.NotFoundRecordInBD;
import ru.practicum.explore_with_me.model.Comment;

import java.util.List;

public interface CommentService {
    /**
     * Создание комментария.
     * @param userId          ID пользователя.
     * @param inputCommentDto входящий комментарий.
     * @return сохранённый комментарий.
     */
    CommentForView addComment(Long userId, CommentUserDto inputCommentDto);

    /**
     * Обновление комментария.
     * @param comId           ID комментария.
     * @param userId          ID пользователя.
     * @param inputCommentDto обновляющий комментарий.
     * @return обновлённый комментарий.
     */
    CommentForView updateComment(Long comId, Long userId, CommentUserDto inputCommentDto);

    /**
     * Получить комментарий по ID.
     * @param comId  ID комментария.
     * @param userId ID пользователя.
     * @return комментарий.
     */
    CommentForView getCommentById(Long userId, Long comId);

    /**
     * Удалить комментарий пользователем.
     * @param comId  ID комментария.
     * @param userId ID пользователя.
     */
    void deleteCommentByUser(Long comId, Long userId);

    /**
     * Удалить комментарий администратором.
     * @param comId ID комментария.
     */
    void deleteCommentByAdmin(Long comId);

    /**
     * Получить список комментариев к событию.
     * @param eventId ID события.
     * @return список комментариев.
     */
    List<CommentForView> getCommentsForEvent(Long eventId, int from, int size);

    /**
     * Получить комментарий из БД по ID или выбросить исключение NotFoundRecordInBD.
     * @param comId   ID комментария.
     * @param message сообщение об ошибке с параметром в виде %d (ID комментария). Это сообщение можно сделать Null.
     * @return комментарий.
     * @throws NotFoundRecordInBD комментарий не найден в БД.
     */
    Comment getCommentOrThrow(Long comId, String message);
}

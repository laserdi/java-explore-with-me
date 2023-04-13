package ru.practicum.explore_with_me.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.comment.CommentForView;
import ru.practicum.explore_with_me.dto.comment.CommentUserDto;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.handler.exceptions.FoundConflictInDB;
import ru.practicum.explore_with_me.handler.exceptions.NotFoundRecordInBD;
import ru.practicum.explore_with_me.handler.exceptions.OperationFailedException;
import ru.practicum.explore_with_me.handler.exceptions.WrongFieldValueException;
import ru.practicum.explore_with_me.mapper.CommentMapper;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.CommentRepository;
import ru.practicum.explore_with_me.service.event.EventService;
import ru.practicum.explore_with_me.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventService eventService;
    private final UserService userService;
    private final CommentMapper commentMapper;

    /**
     * Создание комментария.
     * @param userId          ID пользователя.
     * @param inputCommentDto входящий комментарий.
     * @return сохранённый комментарий.
     * @throws NotFoundRecordInBD в базе данных не найден пользователь или комментарий.
     */
    @Override
    @Transactional
    public CommentForView addComment(Long userId, CommentUserDto inputCommentDto) {
        User userFromDb = userService.getUserOrThrow(userId, "При создании комментария пользователем " +
                "в базе данных не найден пользователь с ID = %d.");
        Event eventFromDb = eventService.getEventOrThrow(inputCommentDto.getEventId(),
                "При создании комментария пользователем в базе данных не найдено событие с ID = %d.");
        Comment comment = commentMapper.mapToModelFromDto(inputCommentDto, eventFromDb, userFromDb);
        comment.setCreatedOn(LocalDateTime.now());
        System.out.println("Печать комментария: \t" + comment);
        Comment result = commentRepository.save(comment);
        log.info("Создан комментарий с ID = {} в БД.", result.getId());
        return commentMapper.mapToView(result);
    }

    /**
     * Получить комментарий по ID.
     * @param userId ID пользователя.
     * @param comId  ID комментария.
     * @return комментарий.
     * @throws NotFoundRecordInBD в базе данных не найден пользователь или комментарий.
     */
    @Override
    public CommentForView getCommentById(Long userId, Long comId) {
        User userFromDb = userService.getUserOrThrow(userId, "При получении комментария пользователем " +
                "в базе данных не найден пользователь с ID = %d.");
        Comment commentFromDb = getCommentOrThrow(comId, "При получении комментария пользователем " +
                "по ID = %d этот комментарий не найден в БД.");
        CommentForView result = commentMapper.mapToView(commentFromDb);
        log.info("Отправлен результат запроса комментария по ID = {}", comId);
        return result;
    }

    /**
     * Удалить комментарий пользователем.
     * @param comId  ID комментария.
     * @param userId ID пользователя.
     * @throws OperationFailedException пользователь не является автором комментария.
     * @throws NotFoundRecordInBD       комментарий не найден в БД.
     */
    @Override
    @Transactional
    public void deleteCommentByUser(Long comId, Long userId) {
        UserDto userFromDb = userService.check(userId, "При удалении комментария пользователем " +
                "в базе данных не найден пользователь с ID = %d.");
        Comment commentFromDb = getCommentOrThrow(comId, "При удалении комментария пользователем " +
                "комментарий с ID = %d не найден в БД.");
        if (checkAuthorComment(commentFromDb, userId)) {
            commentRepository.deleteById(comId);
            log.info("Из БД пользователем с ID = {} удалён комментарий с ID = {}", userId, comId);
            return;
        }
        throw new OperationFailedException(String.format("Ошибка удаления комментария. Пользователь с ID = %d " +
                        "не является автором комментария с ID = %d. Его автор с ID = %d",
                userId, comId, commentFromDb.getUser().getId()));
    }

    /**
     * Удалить комментарий администратором.
     * @param comId ID комментария.
     * @throws NotFoundRecordInBD комментарий не найден в БД.
     */
    @Override
    @Transactional
    public void deleteCommentByAdmin(Long comId) {
        commentRepository.findById(comId).orElseThrow(() -> new NotFoundRecordInBD(String.format(
                "При удалении комментария с ID = %d администратором комментарий не найден в БД.", comId)));
        System.out.println("Найден комментарий перед удалением.");
        commentRepository.deleteById(comId);
        log.info("Администратором выполнено удаление комментария с ID = {}.", comId);
    }

    /**
     * Обновление комментария.
     * @param comId           ID обновляемого комментария.
     * @param userId          ID пользователя.
     * @param inputCommentDto обновляющий комментарий.
     * @return обновлённый комментарий.
     */
    @Override
    @Transactional
    public CommentForView updateComment(Long comId, Long userId, CommentUserDto inputCommentDto) {
        UserDto userFromDb = userService.check(userId, "При получении комментария пользователем " +
                "в базе данных не найден пользователь с ID = %d.");
        Comment commentFromDb = getCommentOrThrow(comId, "При получении комментария пользователем " +
                "комментарий с ID = %d не найден в БД.");
        checkAuthorCommentOrThrow(userId, commentFromDb);

        Comment comment = updateFieldsByUser(inputCommentDto, commentFromDb);
        Comment result = commentRepository.save(comment);

        log.info("Выполнено обновление комментария с ID = {}.", comId);
        return commentMapper.mapToView(result);
    }

    /**
     * Получить список комментариев к событию.
     * @param eventId ID события.
     * @return список комментариев.
     */
    @Override
    public List<CommentForView> getCommentsForEvent(Long eventId, int from, int size) {
        Pageable pageable = PageRequest.of(
                from == 0 ? 0 : (from / size), size);
        List<Comment> comments = commentRepository.findAllByEvent_Id(eventId, pageable);
        List<CommentForView> result = commentMapper.mapFromModelLisToViewList(comments);
        log.info("Выдан список комментариев к событию с ID = {}, состоящий из {} комментариев.",
                eventId, result.size());
        return result;
    }

    /**
     * Получить комментарий из БД по ID или выбросить исключение NotFoundRecordInBD.
     * @param comId   ID комментария.
     * @param message сообщение об ошибке с параметром в виде %d (ID комментария). Это сообщение можно сделать Null.
     * @return комментарий.
     * @throws NotFoundRecordInBD комментарий не найден в БД.
     */
    @Override
    public Comment getCommentOrThrow(Long comId, String message) {
        if (message == null || message.isBlank()) {
            message = "В БД не найден комментарий с ID = %d.";
        }
        String finalMessage = message;
        return commentRepository.findById(comId).orElseThrow(
                () -> new NotFoundRecordInBD(String.format(finalMessage, comId)));
    }

    /**
     * Метод обновления полей комментария с генерацией исключения, если нового текста нет.
     * @param newComment новый комментарий.
     * @param oldComment старый комментарий.
     * @return обновлённый комментарий.
     * @throws WrongFieldValueException не передан текст для обновления.
     */
    private Comment updateFieldsByUser(CommentUserDto newComment, Comment oldComment) {
        if (newComment.getText() == null || newComment.getText().isBlank()) {
            throw new WrongFieldValueException("При изменении комментария не передан его текст.");
        }
        String text = newComment.getText();
        return oldComment.toBuilder()
                .text(text)
                .editedOn(LocalDateTime.now())
                .isEdited(true).build();
    }

    /**
     * Является ли автором комментария пользователь с ID.
     * @param comment комментарий.
     * @param userId  ID пользователя.
     * @return True - это автор. False - это не автор.
     */
    private boolean checkAuthorComment(Comment comment, Long userId) {
        return comment.getUser().getId().equals(userId);
    }

    /**
     * Проверить пользователя на его авторство комментария.
     * @param userId  ID пользователя.
     * @param comment комментарий.
     * @throws FoundConflictInDB пользователь не автор комментария.
     */
    private void checkAuthorCommentOrThrow(Long userId, Comment comment) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new FoundConflictInDB(String.format("Пользователь с ID = %d не является автором" +
                            " комментария с ID = %d. Настоящий автор с ID = %d",
                    comment.getId(), userId, comment.getUser().getId()));
        }
    }
}

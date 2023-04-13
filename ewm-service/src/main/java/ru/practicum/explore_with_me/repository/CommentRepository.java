package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.dto.comment.CommentEvent;
import ru.practicum.explore_with_me.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select count(c) from Comment c where c.event.id = ?1")
    Long countCommentsForEvent(Long id);

    List<Comment> getByEvent_Id(Long id, Pageable pageable);

    List<Comment> findAllByEvent_Id(Long id, Pageable pageable);

    @Query("select new ru.practicum.explore_with_me.dto.comment.CommentEvent(c.event.id, count(c))" +
            " from Comment c where c.event.id in ?1 group by c.event.id")
    List<CommentEvent> getCommentsEvents(List<Long> eventIds);


}
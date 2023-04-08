package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explore_with_me.model.Event;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
//    Page<Event> findByFilter(EventFilter eventFilter, Pageable pageable);
    Optional<Event> findByIdAndInitiator_Id(Long eventId, Long userId);
}

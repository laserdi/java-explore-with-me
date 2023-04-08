package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}

package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);

    /**
     * Поиск пользователей по списку ID.
     * @param ids список ID.
     * @return список пользователей.
     */
    Page<User> findAllByIdIn(List<Long> ids, Pageable pageable);
}

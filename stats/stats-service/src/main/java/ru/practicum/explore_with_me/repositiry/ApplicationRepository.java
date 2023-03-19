package ru.practicum.explore_with_me.repositiry;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.model.Application;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByApp(String appName);
}
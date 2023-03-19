package ru.practicum.explore_with_me.repositiry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.dto.StatDto;
import ru.practicum.explore_with_me.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Long> {
    /**
     * Если нет эндпоинтов, то вывести список всех уникальных эндпоинтов и их посещений
     * пользователями с уникальными IP-адресами.
     * <p><a href="https://teletype.in/@javalib/piKqa7X3QWL#P1GP">Источник для изменения типа возвр. объекта.</a></p>
     * @param start момент начала периода.
     * @param end   момент окончания периода.
     * @return статистику в формате другого класса (с подсчётом посещений).
     */
    //@Query("select s from Stat s where s.timestamp > ?1 and s.timestamp < ?2")
    @Query("select new ru.practicum.explore_with_me.dto.StatDto(s.app, s.uri, count (distinct s.ip))" +
            "from Stat s where s.timestamp > ?1 and s.timestamp < ?2 " +
            "group by s.app, s.uri " +
            "order by count (s.ip) desc")
    List<StatDto> findAllUniqueWhenUriIsEmpty(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explore_with_me.dto.StatDto(s.app, s.uri, count (distinct s.ip)) "
            + "from Stat s where s.timestamp > ?1 and s.timestamp < ?2 "
            + "and s.uri in ?3"
            + "group by s.app, s.uri "
            + "order by count (s.ip) desc ")
    List<StatDto> findAllUniqueWhenUriIsNotEmpty(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.explore_with_me.dto.StatDto(s.app, s.uri, count(s.ip)) "
            + "from Stat s where s.timestamp > ?1 and s.timestamp < ?2 "
            + " group by s.app, s.uri "
            + " order by count(s.ip) desc")
    List<StatDto> findAllWhenUriIsNotEmpty(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explore_with_me.dto.StatDto(s.app, s.uri, count (s.ip))"
            + "from Stat s where s.timestamp > ?1 and s.timestamp < ?2 and s.uri in ?3"
            + " group by s.app, s.uri "
            + "order by count (s.ip) desc")
    List<StatDto> findAllWhenStarEndUris(LocalDateTime start, LocalDateTime end, List<String> urs);
}
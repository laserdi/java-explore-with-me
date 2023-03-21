package ru.practicum.explore_with_me.repositiry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.dto.StatWithHits;
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
    @Query("select new ru.practicum.explore_with_me.dto.StatWithHits(s.app.app, s.uri, count (distinct s.ip))" +
            "from Stat s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.app.app, s.uri " +
            "order by count (distinct s.ip) desc")
    List<StatWithHits> findAllUniqueWhenUriIsEmpty(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explore_with_me.dto.StatWithHits(s.app.app, s.uri, count (distinct s.ip)) "
            + "from Stat s "
            + "where s.timestamp between ?1 and ?2 "
            + "and s.uri in (?3)"
            + "group by s.app.app, s.uri "
            + "order by count (distinct s.ip) desc ")
    List<StatWithHits> findAllUniqueWhenUriIsNotEmpty(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.explore_with_me.dto.StatWithHits(s.app.app, s.uri, count(s.ip)) "
            + "from Stat s where s.timestamp between ?1 and ?2 "
            + " group by s.app.app, s.uri "
            + " order by count(s.ip) desc")
    List<StatWithHits> findAllWhenUriIsEmpty(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explore_with_me.dto.StatWithHits(s.app.app, s.uri, count (s.ip))"
            + "from Stat s "
            + "where s.timestamp between ?1 and ?2 "
            + "and s.uri in (?3)"
            + "group by s.app.app, s.uri "
            + "order by count (s.ip) desc")
    List<StatWithHits> findAllWhenStarEndUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
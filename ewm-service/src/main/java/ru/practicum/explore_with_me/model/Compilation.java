package ru.practicum.explore_with_me.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Подборки событий.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    /**
     * Идентификатор подборки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;

    /**
     * Список событий входящих в подборку, используется EventShortDto - Краткая информация о событии).
     */
    @ManyToMany // TODO: 29.03.2023 Попытаться сделать LAZY.
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "comp_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;

    /**
     * Закреплена ли подборка на главной странице сайта?
     */
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
    /**
     * <p>Заголовок подборки</p>
     * example: Летние концерты
     */
    @Column(name = "title", nullable = false)
    private String title;
}

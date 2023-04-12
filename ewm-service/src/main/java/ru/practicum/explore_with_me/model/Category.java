package ru.practicum.explore_with_me.model;

import lombok.*;

import javax.persistence.*;

/**
 * <p>Категория события.</p>
 * {
 * <p>"id": 1,</p>
 * <p>"name": "Концерты"</p>
 * }
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    /**
     * Идентификатор категории.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id", nullable = false)
    private Long id;
    /**
     * Имя категории.
     */
    @Column(name = "cat_name", nullable = false)
    private String name;
}

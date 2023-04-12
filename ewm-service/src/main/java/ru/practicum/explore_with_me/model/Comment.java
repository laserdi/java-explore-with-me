package ru.practicum.explore_with_me.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    /**
     * Дата редактирования.
     * <p>Если нулл, то значит оригинал.</p>
     */
    @Column(name = "edited_On")
    private LocalDateTime editedOn;

    /**
     * Отредактирован ли комментарий?
     * <p>true - отредактирован.</p>
     * <p>false - без редакции.</p>
     */
    private boolean isEdited;
}

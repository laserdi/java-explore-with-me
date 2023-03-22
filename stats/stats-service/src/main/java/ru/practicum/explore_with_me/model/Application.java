package ru.practicum.explore_with_me.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_id", nullable = false)
    private Long id;
    @Column(name = "app", nullable = false)
    private String app;

    public Application(String app) {
        this.app = app;
    }
}

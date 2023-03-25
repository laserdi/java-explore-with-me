package ru.practicum.explore_with_me.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * email*	string
 * example: petrov.i@practicummail.ru
 * Почтовый адрес
 *
 * id	integer($int64)
 * readOnly: true
 * example: 1
 * Идентификатор
 *
 * name*	string
 * example: Петров Иван
 * Имя
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "username", nullable = false)
    private String name;
}

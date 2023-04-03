package ru.practicum.explore_with_me.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Заявка на участие в событии.
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "req_id")
    Long id;
    /**
     * <p>Дата и время создания заявки.</p>
     * <p>created	string</p>
     * example: 2022-09-06T21:10:05.432
     */
    @Column(name = "req_created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    /**
     * <p>Идентификатор события</p>
     * <p>event	integer($int64)</p>
     * example: 1
     */
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    /**
     * <p>Идентификатор пользователя, отправившего заявку.</p>
     * requester	integer($int64)
     * <p>example: 2</p>
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requester;
    /**
     * <p>Статус заявки.</p>
     * <p>status	string</p>
     * example: PENDING
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusRequest statusRequest;
}

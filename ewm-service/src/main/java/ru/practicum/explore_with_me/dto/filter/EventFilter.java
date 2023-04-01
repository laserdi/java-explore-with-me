package ru.practicum.explore_with_me.dto.filter;

import lombok.*;
import ru.practicum.explore_with_me.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFilter {
/*    private List<Long> ids;
    private LocalDateTime createdOn;
    private String name;
    private LocalDateTime eventDate;
    private List<Long> initiators;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private List<EventState> eventStates;
    private String title;
    private Integer views;*/
    /**
     * список id пользователей, чьи события нужно найти.
     */
    private List<Long> userIds;

    /**
     * список состояний в которых находятся искомые события.
     */
    private List<EventState> states;
    /**
     * список id категорий в которых будет вестись поиск.
     */
    private List<Long> categories;
    /**
     * Надо ли платить за участие?
     */
    private Boolean paid;
    /**
     * дата и время не раньше которых должно произойти событие.
     */
    private LocalDateTime rangeStart;
    /**
     * дата и время не позже которых должно произойти событие.
     */
    private LocalDateTime rangeEnd;
}

package ru.practicum.explore_with_me.dto.comment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent {
    private Long eventId;
    private Long commentCount;
}

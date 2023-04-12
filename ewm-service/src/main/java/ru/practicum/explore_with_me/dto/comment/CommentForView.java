package ru.practicum.explore_with_me.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentForView {
    private Long id;
    private String text;
    private Long eventId;
    private Long userId;
    @JsonProperty("created")
    private LocalDateTime createdOn;
    @JsonProperty("edited")
    private LocalDateTime editedOn;
}

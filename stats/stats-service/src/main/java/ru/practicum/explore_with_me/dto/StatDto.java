package ru.practicum.explore_with_me.dto;

import lombok.*;
import ru.practicum.explore_with_me.model.Application;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StatDto {
    private Application app;
    private String uri;
    private Long hits;
}
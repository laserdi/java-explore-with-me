package ru.practicum.explore_with_me.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StatWithHits {
    private String app;
    private String uri;
    private Long hits;
}
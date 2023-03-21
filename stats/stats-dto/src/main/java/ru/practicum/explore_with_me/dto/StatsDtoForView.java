package ru.practicum.explore_with_me.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class StatsDtoForView {
    /**
     * Название сервиса.
     */
    private String app;
    /**
     * URI сервиса.
     */
    private String uri;
    /**
     * Количество просмотров.
     */
    private Long hits;
}

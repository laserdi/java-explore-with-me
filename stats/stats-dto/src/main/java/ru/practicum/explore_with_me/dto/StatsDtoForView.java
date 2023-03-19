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
    String app;
    /**
     * URI сервиса.
     */
    String uri;
    /**
     * Количество просмотров.
     */
    Long hits;
}

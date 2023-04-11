package ru.practicum.explore_with_me.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Статус события.
 */
@Getter
public enum EventState {
    /**
     * Событие в ожидании.
     */
    PENDING,
    /**
     * Событие опубликовано.
     */
    PUBLISHED,
    /**
     * Событие отменено.
     */
    CANCELED;

    public static List<EventState> fromListString(List<String> states) {
        List<EventState> result = new ArrayList<>();
        if (states == null || states.isEmpty()) {
            return result;
        }
        return states.stream().map(EventState::valueOf).collect(Collectors.toList());
    }
}

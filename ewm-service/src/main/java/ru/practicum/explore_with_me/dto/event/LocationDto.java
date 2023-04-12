package ru.practicum.explore_with_me.dto.event;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Embeddable //Класс будет внедрён другими сущностями.
public class LocationDto {
    /**
     * Географическая широта.
     */
    private Float lat;
    /**
     * Географическая долгота.
     */
    private Float lon;
}

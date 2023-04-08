package ru.practicum.explore_with_me.dto.request;

import lombok.*;
import ru.practicum.explore_with_me.model.StatusRequest;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private StatusRequest status;
}

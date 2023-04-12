package ru.practicum.explore_with_me.dto.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ViewsForMapper {
    private Integer viewsForMapper;
    private Integer confirmedRequestsForMapper;
}

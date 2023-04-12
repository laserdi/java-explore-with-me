package ru.practicum.explore_with_me.dto.compilation;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    @Size(max = 200)
    private String title;
}

package ru.practicum.ewm_main.compilations.model.dto;


import lombok.*;
import ru.practicum.ewm_main.events.model.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    @NotNull
    private Long id;
    @NotBlank
    private String title;
    @NotNull
    private Boolean pinned;
    private List<EventShortDto> events;
}

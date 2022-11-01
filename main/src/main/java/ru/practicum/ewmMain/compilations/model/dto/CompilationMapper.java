package ru.practicum.ewmMain.compilations.model.dto;

import ru.practicum.ewmMain.compilations.model.Compilation;
import ru.practicum.ewmMain.events.model.dto.EventShortDto;

import java.util.List;

public class CompilationMapper {
    public static CompilationDto toCompilationDTO(Compilation compilation, List<EventShortDto> eventList) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                eventList
        );
    }
}

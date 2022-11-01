package ru.practicum.ewmMain.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmMain.compilations.model.dto.CompilationDto;
import ru.practicum.ewmMain.compilations.model.dto.NewCompilationDto;
import ru.practicum.ewmMain.compilations.service.CompilationServiceImpl;

@RestController
@Slf4j
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationServiceImpl compilationService;

    @PostMapping()
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilation) {
        log.info("Добавление подборки - {}", newCompilation.getTitle());
        return compilationService.createCompilation(newCompilation);
    }

    @DeleteMapping(value = "/{compId}")
    public void deleteCompilation(@PathVariable("compId") Long id) {
        log.info("Удаление подборки - {}", id);
        compilationService.deleteCompilation(id);
    }

    @DeleteMapping(value = "/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable("compId") Long id, @PathVariable("eventId") Long eventId) {
        log.info("Удаление события - {} из подборки - {}", eventId, id);
        compilationService.deleteEventFromCompilation(id, eventId);
    }

    @PatchMapping(value = "/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable("compId") Long id, @PathVariable("eventId") Long eventId) {
        log.info("Добавление события - {} в подборку - {}", eventId, id);
        compilationService.addEventToCompilation(id, eventId);
    }

    @DeleteMapping(value = "/{compId}/pin")
    public void deletePinCompilation(@PathVariable("compId") Long id) {
        log.info("Открепить подборку - {}", id);
        compilationService.setPinCompilation(id, Boolean.FALSE);
    }

    @PatchMapping(value = "/{compId}/pin")
    public void addPinCompilation(@PathVariable("compId") Long id) {
        log.info("Закрепить подборку - {}", id);
        compilationService.setPinCompilation(id, Boolean.TRUE);
    }
}

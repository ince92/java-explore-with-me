package ru.practicum.ewm_main.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.compilations.model.dto.CompilationDto;
import ru.practicum.ewm_main.compilations.service.CompilationServiceImpl;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {
    private final CompilationServiceImpl compilationService;

    @GetMapping()
    public List<CompilationDto> getCompilations(@RequestParam(name = "pinned", defaultValue = "false") Boolean pinned,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        log.info("Получить подборки");
        return compilationService.getCompilations(pinned, pageRequest);
    }

    @GetMapping(value = "/{compId}")
    public CompilationDto getCompilationById(@PathVariable("compId") Long id) {
        log.info("Получить подборку по идентификатору - {}",id);
        return compilationService.getCompilationById(id);
    }
}

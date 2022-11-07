package ru.practicum.ewm_main.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.events.model.EventParams;
import ru.practicum.ewm_main.events.model.dto.EventFullDto;
import ru.practicum.ewm_main.events.model.dto.EventShortDto;
import ru.practicum.ewm_main.events.service.EventServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventServiceImpl eventService;

    @GetMapping()
    public List<EventShortDto> getEvents(@RequestParam(name = "text") String text,
                                         @RequestParam(name = "categories") Long[] categories,
                                         @RequestParam(name = "paid") Boolean paid,
                                         @RequestParam(name = "rangeStart") String rangeStart,
                                         @RequestParam(name = "rangeEnd") String rangeEnd,
                                         @RequestParam(name = "onlyAvailable") Boolean onlyAvailable,
                                         @RequestParam(name = "sort") String sort,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                         HttpServletRequest request) {

        EventParams params = new EventParams(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        log.info("Получаем события");
        return eventService.getEvents(params, request);
    }

    @GetMapping(value = "/{id}")
    public EventFullDto getEventById(@PathVariable("id")  long id, HttpServletRequest request) {
        log.info("Получаем событи по идентификатору - {}",id);
        return eventService.getEventById(id, request);
    }

}

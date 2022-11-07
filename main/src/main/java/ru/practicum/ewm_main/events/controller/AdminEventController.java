package ru.practicum.ewm_main.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.events.model.AdminEventParams;
import ru.practicum.ewm_main.events.model.dto.AdminUpdateEventRequest;
import ru.practicum.ewm_main.events.model.dto.EventFullDto;
import ru.practicum.ewm_main.events.service.EventServiceImpl;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventServiceImpl eventService;

    @GetMapping()
    public List<EventFullDto> getEvents(@RequestParam(name = "users") List<Long> users,
                                        @RequestParam(name = "categories") List<Long> categories,
                                        @RequestParam(name = "states") List<String> states,
                                        @RequestParam(name = "rangeStart") String rangeStart,
                                        @RequestParam(name = "rangeEnd") String rangeEnd,
                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        AdminEventParams params = new AdminEventParams(users, categories, rangeStart, rangeEnd, states, from, size);
        log.info("Получение событий");
        return eventService.getAdminEvents(params);
    }

    @PutMapping(value = "/{eventId}")
    public EventFullDto updateEvent(@PathVariable("eventId") Long eventId,
                                    @RequestBody AdminUpdateEventRequest updateRequest) {
        log.info("Получение события по идентификатору - {}", eventId);
        return eventService.updateAdminEvent(eventId, updateRequest);
    }

    @PatchMapping(value = "/{eventId}/publish")
    public EventFullDto setPublished(@PathVariable("eventId") Long eventId) {
        log.info("Публекуем событие  - {}", eventId);
        return eventService.setPublished(eventId);
    }

    @PatchMapping(value = "/{eventId}/reject")
    public EventFullDto setRejected(@PathVariable("eventId") Long eventId) {
        log.info("Отклоняем событие  - {}", eventId);
        return eventService.setRejected(eventId);
    }


}

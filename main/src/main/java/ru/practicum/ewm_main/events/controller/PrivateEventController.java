package ru.practicum.ewm_main.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.events.model.dto.EventFullDto;
import ru.practicum.ewm_main.events.model.dto.EventShortDto;
import ru.practicum.ewm_main.events.model.dto.NewEventDto;
import ru.practicum.ewm_main.events.model.dto.UpdateEventRequest;
import ru.practicum.ewm_main.events.service.EventServiceImpl;
import ru.practicum.ewm_main.requests.model.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users/{id}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventServiceImpl eventService;

    @GetMapping()
    public List<EventShortDto> getUsersEvents(@PathVariable("id") Long id,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        log.info("Получаем события пользователя - {}", id);
        return eventService.getUsersEvents(id, pageRequest);
    }

    @PatchMapping()
    public EventFullDto updateEvent(@PathVariable("id") Long id,
                                    @RequestBody UpdateEventRequest updateRequest) {
        log.info("Обновляем событие - {}", updateRequest.getEventId());
        return eventService.updateEvent(id, updateRequest);
    }

    @PostMapping()
    public EventFullDto createEvent(@PathVariable("id") Long id,
                                    @RequestBody NewEventDto newEvent) {
        log.info("Добавляем событие - {}", newEvent.getTitle());
        return eventService.createEvent(id, newEvent);
    }

    @GetMapping(value = "/{eventId}")
    public EventFullDto getUsersEvents(@PathVariable("id") Long id, @PathVariable("eventId") Long eventId) {
        log.info("Получаем событие пользователя - {} по идентификатору - {}", id, eventId);
        return eventService.getUsersEventById(id, eventId);
    }

    @PatchMapping(value = "/{eventId}")
    public EventFullDto setCancelledStat(@PathVariable("id") Long id, @PathVariable("eventId") Long eventId) {
        log.info("Отклоняем событие - {}", eventId);
        return eventService.setCancelledState(id, eventId);
    }

    @GetMapping(value = "/{eventId}/requests")
    public List<ParticipationRequestDto> getUsersRequestByEvent(@PathVariable("id") Long id,
                                                                @PathVariable("eventId") Long eventId) {
        log.info("Получаем запросы на участие по событию - {}", eventId);
        return eventService.getUsersRequestByEvent(id, eventId);
    }

    @PatchMapping(value = "/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto setRequestConfirmed(@PathVariable("id") Long id,
                                                       @PathVariable("eventId") Long eventId,
                                                       @PathVariable("reqId") Long reqId) {
        log.info("Подтверждаем запрос на участие - {} по событию - {}", reqId, eventId);
        return eventService.setRequestConfirmed(id, eventId, reqId);
    }

    @PatchMapping(value = "/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto setRequestRejected(@PathVariable("id") Long id,
                                                      @PathVariable("eventId") Long eventId,
                                                      @PathVariable("reqId") Long reqId) {
        log.info("Отклоняем запрос на участие - {} по событию - {}", reqId, eventId);
        return eventService.setRequestRejected(id, eventId, reqId);
    }


}

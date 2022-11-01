package ru.practicum.ewmMain.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmMain.requests.model.dto.ParticipationRequestDto;
import ru.practicum.ewmMain.requests.service.RequestServiceImpl;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users/{id}/requests")
@RequiredArgsConstructor
public class PrivateRequestsController {

    private final RequestServiceImpl requestService;

    @GetMapping()
    public List<ParticipationRequestDto> getRequests(@PathVariable("id") Long id) {
        log.info("Получаем запросы на участие");
        return requestService.getRequests(id);
    }

    @PostMapping()
    public ParticipationRequestDto createRequest(@PathVariable("id") Long id, @RequestParam("eventId") Long eventId) {
        log.info("Добавляем запрос на участие от пользователя - {}", id);
        return requestService.createRequest(id, eventId);
    }

    @PatchMapping(value = "/{requestId}/cancel")
    public ParticipationRequestDto canceledRequest(@PathVariable("id") Long id,
                                                   @PathVariable("requestId") Long requestId) {
        log.info("Отклоняем запрос на участие  - {}", id);
        return requestService.canceledRequest(id, requestId);
    }
}

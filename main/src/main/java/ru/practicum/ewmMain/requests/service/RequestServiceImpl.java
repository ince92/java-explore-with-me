package ru.practicum.ewmMain.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewmMain.requests.model.Request;
import ru.practicum.ewmMain.requests.model.RequestStatus;
import ru.practicum.ewmMain.requests.model.dto.ParticipationRequestDto;
import ru.practicum.ewmMain.events.repository.EventRepository;
import ru.practicum.ewmMain.exception.NotFoundException;
import ru.practicum.ewmMain.exception.ValidationException;
import ru.practicum.ewmMain.events.model.Event;
import ru.practicum.ewmMain.events.model.EventState;
import ru.practicum.ewmMain.requests.model.dto.RequestMapper;
import ru.practicum.ewmMain.requests.repository.RequestRepository;
import ru.practicum.ewmMain.users.model.User;
import ru.practicum.ewmMain.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    public List<ParticipationRequestDto> getRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        return requestRepository.getRequestsByRequesterId(user.getId()).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());

    }

    public ParticipationRequestDto createRequest(Long id, Long eventId) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        if (requestRepository.getRequestByRequesterIdAndEventId(id, eventId).isPresent()) {
            throw new ValidationException("Повторный запрос!");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));
        if (event.getInitiator().getId().equals(id)) {
            throw new ValidationException("Нельзя отправить запрос на свое событие!");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Нельзя отправить запрос на неопубликованное собьытие!");
        }
        int confirmedRequests = requestRepository.getRequestsByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size();

        int limit = event.getParticipantLimit();

        if (limit == confirmedRequests) {
            throw new ValidationException("Лимит запросов достигнут");
        }

        Request request = new Request(0L, RequestStatus.PENDING, user, LocalDateTime.now(), event);
        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    public ParticipationRequestDto canceledRequest(Long id, Long requestId) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с таким id не найден!"));
        if (!request.getRequester().getId().equals(user.getId())) {
            throw new ValidationException("Нельзя отменить чужой запрос!");
        }
        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

}

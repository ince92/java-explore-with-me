package ru.practicum.ewm_main.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main.events.model.*;
import ru.practicum.ewm_main.events.model.dto.*;
import ru.practicum.ewm_main.events.repository.EventRepository;
import ru.practicum.ewm_main.exception.NotFoundException;
import ru.practicum.ewm_main.exception.ValidationException;
import ru.practicum.ewm_main.requests.model.Request;
import ru.practicum.ewm_main.requests.model.RequestStatus;
import ru.practicum.ewm_main.requests.model.dto.ParticipationRequestDto;
import ru.practicum.ewm_main.stats.StatService;
import ru.practicum.ewm_main.categories.model.Category;
import ru.practicum.ewm_main.categories.repository.CategoryRepository;
import ru.practicum.ewm_main.events.repository.LocationRepository;
import ru.practicum.ewm_main.requests.model.dto.RequestMapper;
import ru.practicum.ewm_main.requests.repository.RequestRepository;
import ru.practicum.ewm_main.users.model.User;
import ru.practicum.ewm_main.users.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final StatService statService;

    public List<EventShortDto> getEvents(EventParams params, HttpServletRequest request) {
        List<Event> eventList = eventRepository.getEvents(params.getText(), params.getCategories(), params.getPaid(),
                params.getRangeStart(), params.getRangeEnd(), params.getPageRequest());

        statService.hit(request.getRequestURI(), request.getRemoteAddr());

        Map<Long, Long> viewsStat = statService.getViews(eventList, false);

        return eventList.stream().map(e -> EventMapper.toEventShortDto(e, requestRepository.getConfirmedRequests(e.getId()),
                viewsStat.getOrDefault(e.getId(), 0L))).collect(Collectors.toList());
    }

    public EventFullDto getEventById(long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));
        if (event.getState() != EventState.PUBLISHED) {
            throw new ValidationException("Событие не опубликовано!");
        }

        statService.hit(request.getRequestURI(), request.getRemoteAddr());

        Long viewsStat = statService.getView(event, false);

        return EventMapper.toEventFullDto(event, requestRepository.getConfirmedRequests(event.getId()), viewsStat);
    }

    public List<EventShortDto> getUsersEvents(long id, Pageable page) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        List<Event> eventList = eventRepository.getEventsByInitiatorId(user.getId(), page);

        Map<Long, Long> viewsStat = statService.getViews(eventList, false);

        return eventList.stream().map(e -> EventMapper.toEventShortDto(e, requestRepository.getConfirmedRequests(e.getId()),
                viewsStat.getOrDefault(e.getId(), 0L))).collect(Collectors.toList());


    }

    public EventFullDto updateEvent(Long id, UpdateEventRequest updateRequest) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Event event = eventRepository.findById(updateRequest.getEventId()).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));

        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new ValidationException("Невозможно обновить чужое событие!");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new ValidationException("Невозможно обновить опубликованное событие!");
        }
        if (event.getDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Невозможно обновить событие! До нег оосталось менее 2 часов!");
        }

        if (event.getState() == EventState.CANCELED) {
            event.setState(EventState.PENDING);
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }

        if (updateRequest.getEventDate() != null) {
            event.setDate(LocalDateTime.parse(updateRequest.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategory()).orElseThrow(() ->
                    new NotFoundException("Категория с таким id не найдена!"));

            event.setCategory(category);
        }
        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }

        eventRepository.save(event);

        Long viewsStat = statService.getView(event, false);

        return EventMapper.toEventFullDto(event, requestRepository.getConfirmedRequests(event.getId()), viewsStat);
    }

    public EventFullDto createEvent(Long id, NewEventDto newEvent) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Category category = categoryRepository.findById(newEvent.getCategory()).orElseThrow(() ->
                new NotFoundException("Категория с таким id не найдена!"));
        Double lat = newEvent.getLocation().getLat();
        Double lon = newEvent.getLocation().getLon();
        Optional<Location> locationOptional = locationRepository.getLocationByLatAndLon(lat, lon);
        Location location;
        location = locationOptional.orElseGet(() -> locationRepository.save(new Location(0L, lat, lon)));
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toEvent(newEvent, location, category, user)), 0L, 0L);

    }

    public EventFullDto getUsersEventById(Long id, Long eventId) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));

        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new ValidationException("Нет доступа к чужому событию!");
        }
        Long viewsStat = statService.getView(event, false);
        return EventMapper.toEventFullDto(event, requestRepository.getConfirmedRequests(event.getId()), viewsStat);
    }

    public EventFullDto setCancelledState(Long id, Long eventId) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));

        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new ValidationException("Нет доступа к чужому событию!");
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("Можно отклонить событие только со статусом PENDING ");
        }
        event.setState(EventState.CANCELED);

        Long viewsStat = statService.getView(event, false);
        return EventMapper.toEventFullDto(event, requestRepository.getConfirmedRequests(event.getId()), viewsStat);
    }

    public List<ParticipationRequestDto> getUsersRequestByEvent(Long id, Long eventId) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));

        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new ValidationException("Пользователь не является создатиелем события!");
        }


        return requestRepository.getAllByEventId(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());

    }

    public ParticipationRequestDto setRequestConfirmed(Long id, Long eventId, Long reqId) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));
        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new ValidationException("Нет доступа к чужому событию!");
        }
        Request request = requestRepository.findById(reqId).orElseThrow(() ->
                new NotFoundException("Запрос с таким id не найден!"));

        int confirmedRequests = requestRepository.getRequestsByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size();

        int limit = event.getParticipantLimit();
        if (limit == 0 || !event.getRequestModeration()) {
            throw new ValidationException("не требуется подтверждение");
        }
        if (limit == confirmedRequests) {
            throw new ValidationException("Лимит подтверждений исчерпан");
        }

        request.setStatus(RequestStatus.CONFIRMED);
        requestRepository.save(request);

        if (limit - confirmedRequests == 1) {
            List<Request> requestList = requestRepository.getRequestsByEventIdAndNotStatus(eventId);
            for (Request requestRej : requestList) {
                requestRej.setStatus(RequestStatus.REJECTED);
                requestRepository.save(requestRej);
            }
        }
        return RequestMapper.toParticipationRequestDto(request);

    }

    public ParticipationRequestDto setRequestRejected(Long id, Long eventId, Long reqId) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));
        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new ValidationException("Нет доступа к чужому событию!");
        }
        Request request = requestRepository.findById(reqId).orElseThrow(() ->
                new NotFoundException("Запрос с таким id не найден!"));
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);
        return RequestMapper.toParticipationRequestDto(request);
    }

    public List<EventFullDto> getAdminEvents(AdminEventParams params) {
        List<Event> eventList = eventRepository.getAdminEvents(params.getUsers(), params.getCategories(),
                params.getRangeStart(), params.getRangeEnd(), params.getStates(),params.getPageRequest());

        Map<Long, Long> viewsStat = statService.getViews(eventList, false);

        return eventList.stream().map(e -> EventMapper.toEventFullDto(e, requestRepository.getConfirmedRequests(e.getId()),
                viewsStat.getOrDefault(e.getId(), 0L))).collect(Collectors.toList());
    }

    public EventFullDto updateAdminEvent(Long id, AdminUpdateEventRequest updateRequest) {

        Event event = eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));

        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }

        if (updateRequest.getEventDate() != null) {
            event.setDate(LocalDateTime.parse(updateRequest.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategory()).orElseThrow(() ->
                    new NotFoundException("Категория с таким id не найдена!"));

            event.setCategory(category);
        }
        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getLocation() != null) {
            Double lat = updateRequest.getLocation().getLat();
            Double lon = updateRequest.getLocation().getLon();
            Optional<Location> locationOptional = locationRepository.getLocationByLatAndLon(lat, lon);
            Location location = locationOptional.orElseGet(() -> locationRepository.save(new Location(0L, lat, lon)));

            event.setLocation(location);
        }


        eventRepository.save(event);

        Long viewsStat = statService.getView(event, false);
        return EventMapper.toEventFullDto(event, requestRepository.getConfirmedRequests(event.getId()), viewsStat);

    }

    public EventFullDto setPublished(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));
        event.setPublishedOn(LocalDateTime.now());
        if (event.getPublishedOn().isAfter(event.getDate().plusHours(1))) {
            throw new ValidationException("Слишком поздняя публикация!");
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("Невозможно изменить состояние!");
        }
        event.setState(EventState.PUBLISHED);
        eventRepository.save(event);
        Long viewsStat = statService.getView(event, false);
        return EventMapper.toEventFullDto(event, requestRepository.getConfirmedRequests(event.getId()), viewsStat);
    }

    public EventFullDto setRejected(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));


        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Невозможно изменить состояние!");
        }
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        Long viewsStat = statService.getView(event, false);
        return EventMapper.toEventFullDto(event, requestRepository.getConfirmedRequests(event.getId()), viewsStat);
    }

}
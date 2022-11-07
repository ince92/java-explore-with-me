package ru.practicum.ewm_main.events.model.dto;

import ru.practicum.ewm_main.categories.model.dto.CategoryMapper;
import ru.practicum.ewm_main.users.model.dto.UserMapper;
import ru.practicum.ewm_main.categories.model.Category;
import ru.practicum.ewm_main.events.model.Event;
import ru.practicum.ewm_main.events.model.EventState;
import ru.practicum.ewm_main.events.model.Location;
import ru.practicum.ewm_main.users.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {

    public static EventShortDto toEventShortDto(Event event, Long confirmRequests, Long view) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                confirmRequests,
                event.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                view

        );
    }

    public static EventFullDto toEventFullDto(Event event, Long confirmRequests, Long view) {
        return new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                confirmRequests,
                event.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                event.getDescription(),
                event.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn() != null ? event.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
                event.getRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                view

        );
    }

    public static Event toEvent(NewEventDto event, Location location, Category category, User initiator) {
        return new Event(
                0L,
                category,
                LocalDateTime.now(),
                event.getDescription(),
                LocalDateTime.parse(event.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                initiator,
                location,
                event.getPaid(),
                event.getParticipantLimit(),
                null,
                event.getRequestModeration(),
                EventState.PENDING,
                event.getTitle(),
                event.getAnnotation()

        );
    }

}

package ru.practicum.ewm_main.requests.model.dto;

import ru.practicum.ewm_main.requests.model.Request;

import java.time.format.DateTimeFormatter;

public class RequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
               request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
               request.getEvent().getId(),
               request.getId(),
               request.getRequester().getId(),
               request.getStatus().toString()

        );
    }

}

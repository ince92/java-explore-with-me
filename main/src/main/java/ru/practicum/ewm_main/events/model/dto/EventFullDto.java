package ru.practicum.ewm_main.events.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm_main.categories.model.dto.CategoryDto;
import ru.practicum.ewm_main.events.model.Location;
import ru.practicum.ewm_main.users.model.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    @NotBlank
    private String eventDate;
    private Long id;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private String state;
    @NotBlank
    private String title;
    private Long views;
}

package ru.practicum.ewmMain.events.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AdminEventParams {

    private List<Long> users;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private List<EventState> states;
    private PageRequest pageRequest;

    public AdminEventParams(List<Long> users, List<Long> categories, String rangeStart,
                            String rangeEnd, List<String> states, Integer from, Integer size) {

        this.users = users;
        this.categories = categories;

        if (rangeStart != null && rangeEnd != null) {
            this.rangeStart = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            this.rangeEnd = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            this.rangeStart = LocalDateTime.now();
            this.rangeEnd = LocalDateTime.MAX;
        }
        this.states = states.stream().map(EventState::valueOf).collect(Collectors.toList());
        int page = from / size;

        this.pageRequest = PageRequest.of(page, size);
    }
}

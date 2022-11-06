package ru.practicum.ewm_main.events.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.ewm_main.exception.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class EventParams {

    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private PageRequest pageRequest;

    public EventParams(String text, Long[] categories, Boolean paid, String rangeStart,
                       String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size) {
        this.text = text;
        this.categories = Arrays.asList(categories);
        this.paid = paid;
        if (rangeStart != null && rangeEnd != null) {
            this.rangeStart = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            this.rangeEnd = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            this.rangeStart = LocalDateTime.now();
            this.rangeEnd = LocalDateTime.MAX;
        }
        this.onlyAvailable = onlyAvailable;
        try {
            EventSort thisSort = EventSort.valueOf(sort);

            if (thisSort == EventSort.EVENT_DATE) {
                int page = from / size;
                this.pageRequest = PageRequest.of(page, size, Sort.by("date"));
            } else {
                int page = from / size;
                this.pageRequest = PageRequest.of(page, size, Sort.by("views"));
            }


        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown sort: " + sort);
        }

    }
}

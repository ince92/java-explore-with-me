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
    private EventSort sort;

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
            this.rangeEnd = LocalDateTime.now().plusYears(100);
        }
        this.onlyAvailable = onlyAvailable;
        try {

            if (sort == null) {
                this.sort = EventSort.EVENT_DATE;
            } else {
                this.sort = EventSort.valueOf(sort);
            }
            int page = from / size;
            if (this.sort.equals(EventSort.EVENT_DATE)) {
                this.pageRequest = PageRequest.of(page, size, Sort.by("date"));
            } else {
                this.pageRequest = PageRequest.of(page, size);
            }


        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown sort: " + sort);
        }

    }
}

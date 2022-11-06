package ru.practicum.ewm_stat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_stat.model.EndpointHit;
import ru.practicum.ewm_stat.model.ViewStats;
import ru.practicum.ewm_stat.repository.HitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HitServiceImpl {

    private final HitRepository hitRepository;

    public List<ViewStats> getViews(List<String> uris, Boolean unique, String start, String end) {

        if (unique) {
            return hitRepository.getViewStatsUnique(uris,
                    LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            return hitRepository.getViewStats(uris,
                    LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }


    }

    public void save(EndpointHit newHit) {
        hitRepository.save(newHit);
    }


}

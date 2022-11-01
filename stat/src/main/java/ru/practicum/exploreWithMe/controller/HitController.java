package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.EndpointHitDto;
import ru.practicum.exploreWithMe.model.HitMapper;
import ru.practicum.exploreWithMe.model.ViewStats;
import ru.practicum.exploreWithMe.service.HitServiceImpl;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HitController {

    private final HitServiceImpl hitService;

    @GetMapping(value = "/stats")
    public List<ViewStats> getViews(@RequestParam(name = "uris") List<String> uris,
                                    @RequestParam(name = "unique") Boolean unique,
                                    @RequestParam(name = "start") String start,
                                    @RequestParam(name = "end") String end) {

        return hitService.getViews(uris, unique, start, end);

    }

    @PostMapping(value = "/hit")
    public void hit(@RequestBody EndpointHitDto newHit) {
        hitService.save(HitMapper.toEndpointHit(newHit));
    }


}

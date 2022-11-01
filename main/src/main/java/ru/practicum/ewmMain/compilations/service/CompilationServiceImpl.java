package ru.practicum.ewmMain.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmMain.stats.StatService;
import ru.practicum.ewmMain.compilations.model.Compilation;
import ru.practicum.ewmMain.compilations.model.dto.CompilationDto;
import ru.practicum.ewmMain.compilations.model.dto.NewCompilationDto;
import ru.practicum.ewmMain.events.model.dto.EventMapper;
import ru.practicum.ewmMain.events.model.dto.EventShortDto;
import ru.practicum.ewmMain.events.repository.EventRepository;
import ru.practicum.ewmMain.exception.NotFoundException;
import ru.practicum.ewmMain.compilations.model.dto.CompilationMapper;
import ru.practicum.ewmMain.compilations.repository.CompilationRepository;
import ru.practicum.ewmMain.events.model.Event;
import ru.practicum.ewmMain.requests.repository.RequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final StatService statService;
    private final RequestRepository requestRepository;

    public List<CompilationDto> getCompilations(Boolean pinned, Pageable pageRequest) {
        List<Compilation> compilationList = compilationRepository.getCompilationsByPinned(pinned, pageRequest);
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        for (Compilation compilation : compilationList) {
            List<EventShortDto> eventList = new ArrayList<>();
            if (!compilation.getEvents().isEmpty()) {
                Map<Long, Long> viewsStat = statService.getViews(compilation.getEvents(), false);
                eventList = compilation.getEvents().stream()
                        .map(e -> EventMapper.toEventShortDto(e, requestRepository.getConfirmedRequests(e.getId()),
                                viewsStat.getOrDefault(e.getId(), 0L))).collect(Collectors.toList());
            }
            compilationDtoList.add(CompilationMapper.toCompilationDTO(compilation, eventList));

        }
        return compilationDtoList;
    }

    public CompilationDto getCompilationById(Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Подборка с таким id не найдена!"));
        List<EventShortDto> eventList = new ArrayList<>();
        if (!compilation.getEvents().isEmpty()) {
            Map<Long, Long> viewsStat = statService.getViews(compilation.getEvents(), false);
            eventList = compilation.getEvents().stream()
                    .map(e -> EventMapper.toEventShortDto(e, requestRepository.getConfirmedRequests(e.getId()),
                            viewsStat.getOrDefault(e.getId(), 0L))).collect(Collectors.toList());
        }
        return CompilationMapper.toCompilationDTO(compilation, eventList);
    }

    public CompilationDto createCompilation(NewCompilationDto newCompilation) {
        List<Event> eventList = eventRepository.findAllById(newCompilation.getEvents());
        Compilation compilation = new Compilation(0L, newCompilation.getTitle(), newCompilation.getPinned(), eventList);
        List<EventShortDto> eventDtoList = new ArrayList<>();
        if (!compilation.getEvents().isEmpty()) {
            Map<Long, Long> viewsStat = statService.getViews(eventList, false);
            eventDtoList = compilation.getEvents().stream()
                    .map(e -> EventMapper.toEventShortDto(e, requestRepository.getConfirmedRequests(e.getId()),
                            viewsStat.getOrDefault(e.getId(), 0L))).collect(Collectors.toList());
        }
        return CompilationMapper.toCompilationDTO(compilationRepository.save(compilation), eventDtoList);
    }

    public void deleteCompilation(Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Подборка с таким id не найдена!"));
        compilationRepository.delete(compilation);


    }

    public void deleteEventFromCompilation(Long id, Long eventId) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Подборка с таким id не найдена!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));
        List<Event> eventList = compilation.getEvents();
        eventList.remove(event);
        compilation.setEvents(eventList);
        compilationRepository.save(compilation);
    }

    public void addEventToCompilation(Long id, Long eventId) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Подборка с таким id не найдена!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с таким id не найдено!"));
        List<Event> eventList = compilation.getEvents();
        eventList.add(event);
        compilation.setEvents(eventList);
        compilationRepository.save(compilation);
    }

    public void setPinCompilation(Long id, Boolean pin) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Подборка с таким id не найдена!"));
        compilation.setPinned(pin);
        compilationRepository.save(compilation);

    }
}

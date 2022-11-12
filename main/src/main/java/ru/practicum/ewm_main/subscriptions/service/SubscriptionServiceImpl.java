package ru.practicum.ewm_main.subscriptions.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main.events.model.Event;
import ru.practicum.ewm_main.events.model.dto.EventMapper;
import ru.practicum.ewm_main.events.model.dto.EventShortDto;
import ru.practicum.ewm_main.events.repository.EventRepository;
import ru.practicum.ewm_main.exception.NotFoundException;
import ru.practicum.ewm_main.exception.ValidationException;
import ru.practicum.ewm_main.requests.model.Request;
import ru.practicum.ewm_main.requests.repository.RequestRepository;
import ru.practicum.ewm_main.stats.StatService;
import ru.practicum.ewm_main.subscriptions.model.Subscription;
import ru.practicum.ewm_main.subscriptions.model.SubscriptionDto;
import ru.practicum.ewm_main.subscriptions.model.SubscriptionMapper;
import ru.practicum.ewm_main.subscriptions.model.SubscriptionStatus;
import ru.practicum.ewm_main.subscriptions.repository.SubscriptionRepository;
import ru.practicum.ewm_main.users.model.User;
import ru.practicum.ewm_main.users.model.dto.UserMapper;
import ru.practicum.ewm_main.users.model.dto.UserShortDto;
import ru.practicum.ewm_main.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    private final EventRepository eventRepository;
    private final StatService statService;
    private final RequestRepository requestRepository;

    public SubscriptionDto createSubscription(Long id, Long authorId) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        User author = userRepository.findById(authorId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));

        if (subscriptionRepository.getSubscriptionByAuthorIdAndSubscriberId(author.getId(), user.getId()).isPresent()) {
            throw new ValidationException("Запрос на подписку уже существует!");
        }

        return SubscriptionMapper.toSubscriptionDto(subscriptionRepository.save(
                new Subscription(0L, user, author, SubscriptionStatus.PENDING)));
    }

    public SubscriptionDto setSubscriptionCancelled(Long id, Long subscriptionId) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() ->
                new NotFoundException("Запрос подписки с таким id не найден!"));

        if (!subscription.getSubscriber().getId().equals(id)) {
            throw new ValidationException("Нельзя отменить чужую подписку");
        }
        subscription.setStatus(SubscriptionStatus.CANCELLED);

        return SubscriptionMapper.toSubscriptionDto(subscriptionRepository.save(subscription));
    }

    public SubscriptionDto setSubscriptionConfirm(Long id, Long subscriptionId) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() ->
                new NotFoundException("Запрос подписки с таким id не найден!"));

        if (!subscription.getAuthor().getId().equals(id)) {
            throw new ValidationException("Нельзя подтвердить подписку на другого автора");
        }

        if (!subscription.getStatus().equals(SubscriptionStatus.PENDING)) {
            throw new ValidationException("Нельзя подтвердить подписку не в статусе PENDING");
        }
        subscription.setStatus(SubscriptionStatus.CONFIRMED);

        return SubscriptionMapper.toSubscriptionDto(subscriptionRepository.save(subscription));
    }

    public SubscriptionDto setSubscriptionReject(Long id, Long subscriptionId) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() ->
                new NotFoundException("Запрос подписки с таким id не найден!"));

        if (!subscription.getAuthor().getId().equals(id)) {
            throw new ValidationException("Нельзя отменить подписку на другого автора");
        }

        subscription.setStatus(SubscriptionStatus.REJECTED);

        return SubscriptionMapper.toSubscriptionDto(subscriptionRepository.save(subscription));
    }

    public List<EventShortDto> getActualEvents(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        List<Event> eventList = eventRepository.getEventsBySubscriber(id, SubscriptionStatus.CONFIRMED, LocalDateTime.now());
        Map<Long, Long> confirmedMap = getConfirmedRequestsList(eventList);
        Map<Long, Long> viewsStat = statService.getViews(eventList, false);
        return eventList.stream().map(e -> EventMapper.toEventShortDto(e, confirmedMap.getOrDefault(e.getId(), 0L),
                viewsStat.getOrDefault(e.getId(), 0L))).collect(Collectors.toList());
    }

    public List<UserShortDto> getSubscribers(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        return userRepository.getSubscribers(id).stream().map(UserMapper::toUserShortDto).collect(Collectors.toList());
    }

    public List<UserShortDto> getSubscriptions(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        return userRepository.getSubscriptions(id).stream().map(UserMapper::toUserShortDto).collect(Collectors.toList());

    }

    private Map<Long, Long> getConfirmedRequestsList(List<Event> eventList) {
        List<Long> ids = new ArrayList<>();

        for (Event event : eventList) {
            ids.add(event.getId());
        }

        List<Request> requestList = requestRepository.getConfirmedRequestsList(ids);
        Map<Long, Long> requestMap = new HashMap<>();
        for (Request request : requestList) {
            requestMap.put(request.getEvent().getId(), requestMap.getOrDefault(request.getEvent().getId(), 0L) + 1L);
        }

        return requestMap;
    }


}

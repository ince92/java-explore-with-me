package ru.practicum.ewm_main.subscriptions.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.events.model.dto.EventShortDto;
import ru.practicum.ewm_main.subscriptions.model.SubscriptionDto;
import ru.practicum.ewm_main.subscriptions.service.SubscriptionServiceImpl;
import ru.practicum.ewm_main.users.model.dto.UserShortDto;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users/{id}/subscribe")
@RequiredArgsConstructor
public class PrivateSubscriptionController {

    private final SubscriptionServiceImpl subscriptionService;

    @PostMapping()
    public SubscriptionDto createSubscription(@PathVariable("id") Long id, @RequestParam("authorId") Long authorId) {
        log.info("Добавляем подписку на пользователя - {}", authorId);
        return subscriptionService.createSubscription(id, authorId);
    }

    @PatchMapping(value = "/{subscribeId}/cancel")
    public SubscriptionDto setSubscriptionCancelled(@PathVariable("id") Long id,
                                                    @PathVariable("subscribeId") Long subscribeId) {
        log.info("Отменяем свою подписку  - {}", subscribeId);
        return subscriptionService.setSubscriptionCancelled(id, subscribeId);
    }

    @PatchMapping(value = "/{subscribeId}/confirm")
    public SubscriptionDto setSubscriptionConfirm(@PathVariable("id") Long id,
                                                  @PathVariable("subscribeId") Long subscribeId) {
        log.info("Подтверждаем подписку  - {}", subscribeId);
        return subscriptionService.setSubscriptionConfirm(id, subscribeId);
    }

    @PatchMapping(value = "/{subscribeId}/reject")
    public SubscriptionDto setSubscriptionReject(@PathVariable("id") Long id,
                                                 @PathVariable("subscribeId") Long subscribeId) {
        log.info("Отклоняем подписку  - {}", subscribeId);
        return subscriptionService.setSubscriptionReject(id, subscribeId);
    }

    @GetMapping(value = "/events")
    public List<EventShortDto> getActualEvents(@PathVariable("id") Long id) {
        log.info("Получаем список событий по подпискам пользователя  - {}", id);
        return subscriptionService.getActualEvents(id);
    }

    @GetMapping(value = "/subscribers")
    public List<UserShortDto> getSubscribers(@PathVariable("id") Long id) {
        log.info("Получаем подписчиков пользователя  - {}", id);
        return subscriptionService.getSubscribers(id);
    }

    @GetMapping(value = "/subscriptions")
    public List<UserShortDto> getSubscriptions(@PathVariable("id") Long id) {
        log.info("Получаем подписки пользователя  - {}", id);
        return subscriptionService.getSubscriptions(id);
    }
}

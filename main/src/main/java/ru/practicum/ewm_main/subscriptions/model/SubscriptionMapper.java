package ru.practicum.ewm_main.subscriptions.model;


public class SubscriptionMapper {
    public static SubscriptionDto toSubscriptionDto(Subscription subscription) {
        return new SubscriptionDto(
                subscription.getSubscriber().getId(),
                subscription.getAuthor().getId(),
                subscription.getStatus().name()
        );
    }
}

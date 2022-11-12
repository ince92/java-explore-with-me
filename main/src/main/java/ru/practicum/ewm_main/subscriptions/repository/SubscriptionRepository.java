package ru.practicum.ewm_main.subscriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_main.subscriptions.model.Subscription;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> getSubscriptionByAuthorIdAndSubscriberId(Long authorId, Long subscriberId);
}

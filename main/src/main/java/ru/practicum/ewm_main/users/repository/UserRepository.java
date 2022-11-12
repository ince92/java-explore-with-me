package ru.practicum.ewm_main.users.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm_main.subscriptions.model.SubscriptionStatus;
import ru.practicum.ewm_main.users.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u " +
            "where ((coalesce(:ids, null) is null or u.id in :ids))")
    List<User> getUsers(List<Long> ids, Pageable pageRequest);

    @Query(value = "select u from User u inner join Subscription s on u.id=s.subscriber.id where s.author.id = :id and s.status =:status")
    List<User> getSubscribers(@Param("id") Long id, @Param("status") SubscriptionStatus status);

    @Query(value = "select u from User u inner join Subscription s on u.id=s.author.id where s.subscriber.id = :id and s.status =:status")
    List<User> getSubscriptions(@Param("id") Long id, @Param("status") SubscriptionStatus status);

}

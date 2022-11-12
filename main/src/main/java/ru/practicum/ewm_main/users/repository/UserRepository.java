package ru.practicum.ewm_main.users.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_main.users.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u " +
            "where ((coalesce(:ids, null) is null or u.id in :ids))")
    List<User> getUsers(List<Long> ids, Pageable pageRequest);

    @Query(value = "select u from User u inner join Subscription s on u.id=s.subscriber.id where s.author.id = :id")
    List<User> getSubscribers(Long id);

    @Query(value = "select u from User u inner join Subscription s on u.id=s.subscriber.id where s.subscriber.id = :id")
    List<User> getSubscriptions(Long id);

}

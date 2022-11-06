package ru.practicum.ewm_main.users.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_main.users.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "select u from User u " +
            "where ((coalesce(:ids, null) is null or u.id in :ids))")
        List<User> getUsers(List<Long> ids, Pageable pagerequest);
}

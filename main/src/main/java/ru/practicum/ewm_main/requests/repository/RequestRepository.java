package ru.practicum.ewm_main.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm_main.requests.model.Request;
import ru.practicum.ewm_main.requests.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {


    List<Request> getAllByEventId(Long id);

    List<Request> getRequestsByEventIdAndStatus(Long id, RequestStatus status);

    @Query(value = "select r from Request r " +
            "where r.status <> 'CONFIRMED' AND r.requester.id = :id")
    List<Request> getRequestsByEventIdAndNotStatus(@Param("id")  Long id);

    List<Request> getRequestsByRequesterId(Long id);

    Optional<Request> getRequestByRequesterIdAndEventId(Long id, Long eventId);

    @Query(value = "select count(r) from Request r " +
            "where r.status = 'CONFIRMED' and r.event.id = :id")
    Long getConfirmedRequests(@Param("id") Long id);

    @Query(value = "select r from Request r " +
            "where r.status = 'CONFIRMED'and r.event.id in :ids")
    List<Request> getConfirmedRequestsList(@Param("ids") List<Long> ids);
}

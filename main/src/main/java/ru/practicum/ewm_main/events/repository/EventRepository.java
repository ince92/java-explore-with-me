package ru.practicum.ewm_main.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm_main.events.model.Event;
import ru.practicum.ewm_main.events.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "select e from Event e " +
            "where ((coalesce(:text, null) is null or lower(e.annotation) like concat('%', lower(:text), '%')) " +
            "or (coalesce(:text, null) is null or lower(e.description) like concat('%', lower(:text), '%'))) " +
            "and (coalesce(:categories, null) is null or e.category.id in :categories) " +
            "and (coalesce(:paid, null) is null or e.paid = :paid) " +
            "and e.date between :rangeStart AND :rangeEnd and e.state = 'PUBLISHED'")
    List<Event> getEvents(@Param("text") String text, @Param("categories") List<Long> categories,
                            @Param("paid") Boolean paid, @Param("rangeStart") LocalDateTime rangeStart,
                            @Param("rangeEnd") LocalDateTime rangeEnd, Pageable page);

    List<Event> getEventsByInitiatorId(long id, Pageable page);

    @Query(value = "select e from Event e " +
            "where ((coalesce(:users, null) is null or e.initiator.id in :users)) " +
            "and (coalesce(:categories, null) is null or e.category.id in :categories) " +
            "and (coalesce(:states, null) is null or e.state in :states) " +
            "and e.date between :rangeStart AND :rangeEnd")
    List<Event> getAdminEvents(@Param("users")  List<Long> users, @Param("categories") List<Long> categories,
                               @Param("rangeStart") LocalDateTime rangeStart,
                               @Param("rangeEnd") LocalDateTime rangeEnd,
                               @Param("states") List<EventState> states, Pageable page);

    List<Event> getEventsByCategoryId(Long id);


}

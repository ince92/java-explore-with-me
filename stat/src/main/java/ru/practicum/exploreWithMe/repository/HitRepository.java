package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.model.EndpointHit;
import ru.practicum.exploreWithMe.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "select new ru.practicum.exploreWithMe.model.ViewStats(e.app, e.uri, count(e.ip)) " +
            "from EndpointHit as e " +
            "where e.timestamp between :start and :end " +
            "and (coalesce(:uris, null) is null or e.uri in :uris) " +
            "GROUP BY e.uri, e.app")
    List<ViewStats> getViewStats(@Param("uris") List<String> uris, @Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);

    @Query(value = "select new ru.practicum.exploreWithMe.model.ViewStats(e.app, e.uri, count(distinct e.ip)) " +
            "from EndpointHit as e " +
            "where e.timestamp between :start and :end " +
            "and (coalesce(:uris, null) is null or e.uri in :uris) " +
            "GROUP BY e.uri, e.app")
    List<ViewStats> getViewStatsUnique(@Param("uris") List<String> uris, @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

}

package ru.practicum.ewm_main.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_main.events.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> getLocationByLatAndLon(Double lat, Double lon);
}

package ru.practicum.ewmMain.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmMain.events.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> getLocationByLatAndLon(Double lat, Double lon);
}

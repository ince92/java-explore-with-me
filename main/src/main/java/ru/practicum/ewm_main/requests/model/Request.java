package ru.practicum.ewm_main.requests.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm_main.events.model.Event;
import ru.practicum.ewm_main.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private RequestStatus status;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "request_requester_id")
    private User requester;
    @Column(name = "request_created")
    private LocalDateTime created;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "request_event_id")
    private Event event;
}


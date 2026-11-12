package dev.seatlock.service;

import dev.seatlock.data.Event;
import dev.seatlock.data.EventRepository;
import dev.seatlock.data.Seat;
import dev.seatlock.data.SeatRepository;
import dev.seatlock.domain.DomainException;
import dev.seatlock.domain.Dtos.EventDto;
import dev.seatlock.domain.Dtos.NewEvent;
import dev.seatlock.domain.Dtos.SeatDto;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {
    private final EntityManager em;
    private final EventRepository events;
    private final SeatRepository seats;

    public EventService(EntityManager em, EventRepository events, SeatRepository seats) {
        this.em = em;
        this.events = events;
        this.seats = seats;
    }

    @Transactional
    public EventDto create(NewEvent input, String createdBy) {
        var e = new Event();
        e.id = UUID.randomUUID();
        e.name = input.name();
        e.venue = input.venue();
        e.startsAt = input.startsAt();
        e.createdBy = createdBy;
        e.createdAt = Instant.now();
        em.persist(e);
        int count = 0;
        for (var s : input.sections()) {
            for (int r = 0; r < s.rows(); r++) {
                for (int n = 1; n <= s.seatsPerRow(); n++) {
                    var seat = new Seat();
                    seat.id = UUID.randomUUID();
                    seat.eventId = e.id;
                    seat.section = s.name();
                    seat.rowLabel = String.valueOf((char) ('A' + r));
                    seat.number = n;
                    seat.priceCents = s.priceCents();
                    em.persist(seat);
                    if (++count % 500 == 0) { // batch the inserts and keep the persistence context small
                        em.flush();
                        em.clear();
                    }
                }
            }
        }
        return new EventDto(e.id, e.name, e.venue, e.startsAt, count);
    }

    /** Spring Cache on Redis (Chapter 25): events never change after creation. */
    @Cacheable(cacheNames = "events", key = "#id")
    @Transactional(readOnly = true)
    public EventDto get(UUID id) {
        var e = events.findById(id).orElseThrow(() -> DomainException.notFound("Event", id));
        return new EventDto(e.id, e.name, e.venue, e.startsAt, (int) seats.countByEventId(id));
    }

    /** The seat layout only; statuses are always read fresh by the controller. */
    @Cacheable(cacheNames = "seat-layouts", key = "#eventId")
    @Transactional(readOnly = true)
    public List<SeatDto> layout(UUID eventId) {
        if (!events.existsById(eventId)) throw DomainException.notFound("Event", eventId);
        return seats.findByEventIdOrderBySectionAscRowLabelAscNumberAsc(eventId).stream()
                .map(s -> new SeatDto(s.id, s.section, s.rowLabel, s.number, s.priceCents, null))
                .toList();
    }
}

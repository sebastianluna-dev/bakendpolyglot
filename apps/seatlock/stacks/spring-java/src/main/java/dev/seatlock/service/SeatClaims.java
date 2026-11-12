package dev.seatlock.service;

import dev.seatlock.AppConfig;
import dev.seatlock.data.EventRepository;
import dev.seatlock.data.Hold;
import dev.seatlock.data.SeatRepository;
import dev.seatlock.domain.DomainException;
import dev.seatlock.domain.Dtos.HoldDto;
import jakarta.persistence.EntityManager;
import java.time.Clock;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * The claim in its own bean, so HoldService can call it through Spring's transactional proxy and
 * handle the rollback outside the transaction (a self-call would bypass @Transactional).
 */
@Component
public class SeatClaims {
    /** Thrown inside the transaction to roll it back; HoldService turns it into seats-unavailable. */
    static final class SeatsTaken extends RuntimeException {
        SeatsTaken() {
            super(null, null, false, false);
        }
    }

    private final EntityManager em;
    private final EventRepository events;
    private final SeatRepository seats;
    private final AppConfig config;
    private final Clock clock;

    public SeatClaims(EntityManager em, EventRepository events, SeatRepository seats, AppConfig config, Clock clock) {
        this.em = em;
        this.events = events;
        this.seats = seats;
        this.config = config;
        this.clock = clock;
    }

    /** All or nothing: one conditional UPDATE whose row count decides (Chapter 23). */
    @Transactional
    public HoldDto claim(String userId, UUID eventId, List<UUID> seatIds) {
        if (!events.existsById(eventId)) throw DomainException.notFound("Event", eventId);
        seats.lockInOrder(eventId, seatIds);
        var now = clock.instant().truncatedTo(ChronoUnit.MICROS);
        var hold = new Hold();
        hold.id = UUID.randomUUID();
        hold.eventId = eventId;
        hold.userId = userId;
        hold.seatIds = seatIds.toArray(UUID[]::new);
        hold.createdAt = now;
        hold.expiresAt = now.plus(Duration.ofSeconds(config.holdTtlSeconds()));
        em.persist(hold);
        em.flush(); // the hold row must exist before seats point at it
        if (seats.claim(eventId, seatIds, hold.id) != seatIds.size()) throw new SeatsTaken(); // runtime exception → rollback
        return HoldService.toDto(hold, now);
    }

}

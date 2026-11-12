package dev.seatlock.service;

import dev.seatlock.data.Hold;
import dev.seatlock.data.HoldRepository;
import dev.seatlock.data.SeatRepository;
import dev.seatlock.domain.DomainException;
import dev.seatlock.domain.Dtos.HoldDto;
import dev.seatlock.domain.HoldStatus;
import java.time.Clock;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HoldService {
    private final SeatClaims claims;
    private final HoldRepository holds;
    private final SeatRepository seats;
    private final Clock clock;

    public HoldService(SeatClaims claims, HoldRepository holds, SeatRepository seats, Clock clock) {
        this.claims = claims;
        this.holds = holds;
        this.seats = seats;
        this.clock = clock;
    }

    static HoldDto toDto(Hold h, java.time.Instant now) {
        return new HoldDto(h.id, h.eventId, Arrays.stream(h.seatIds).map(UUID::toString).sorted().toList(),
                HoldStatus.visible(h.status, h.expiresAt, now), h.expiresAt);
    }

    /** Not transactional: the failed claim's transaction is already rolled back. */
    public HoldDto create(String userId, UUID eventId, List<UUID> seatIds) {
        try {
            return claims.claim(userId, eventId, seatIds);
        } catch (SeatClaims.SeatsTaken e) {
            var free = new HashSet<>(seats.findFree(eventId, seatIds));
            throw DomainException.seatsUnavailable(seatIds.stream().filter(id -> !free.contains(id)).toList());
        }
    }

    @Transactional(readOnly = true)
    public HoldDto get(String userId, UUID id) {
        var hold = holds.findByIdAndUserId(id, userId).orElseThrow(() -> DomainException.notFound("Hold", id));
        return toDto(hold, clock.instant());
    }

    @Transactional
    public void release(String userId, UUID id) {
        if (holds.release(id, userId, clock.instant()) == 0) {
            throw holds.findByIdAndUserId(id, userId).isPresent() ? DomainException.holdNotActive(id) : DomainException.notFound("Hold", id);
        }
        seats.releaseHeld(List.of(id));
    }

    /** The reaper (Chapter 24): idempotent; SKIP LOCKED lets several instances share the work. */
    @Transactional
    public int releaseExpired() {
        var ids = holds.lockExpired(clock.instant());
        if (!ids.isEmpty()) {
            holds.expire(ids);
            seats.releaseHeld(ids);
        }
        return ids.size();
    }

}

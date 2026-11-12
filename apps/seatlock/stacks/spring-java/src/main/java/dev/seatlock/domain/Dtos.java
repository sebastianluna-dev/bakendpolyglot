package dev.seatlock.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** Response shapes (records; Jackson writes their components by name). */
public final class Dtos {
    private Dtos() {}

    public record EventDto(UUID id, String name, String venue, Instant startsAt, int seatCount) {}

    public record SeatDto(UUID id, String section, String row, int number, int priceCents, String status) {
        public SeatDto withStatus(String s) { return new SeatDto(id, section, row, number, priceCents, s); }
    }

    public record SeatMapDto(UUID eventId, List<SeatDto> items) {}

    public record HoldDto(UUID id, UUID eventId, List<String> seatIds, String status, Instant expiresAt) {}

    public record OrderDto(UUID id, UUID holdId, UUID eventId, List<String> seatIds, String status,
                           int totalCents, String currency, String paymentIntentId) {}

    public record Section(String name, int rows, int seatsPerRow, int priceCents) {}

    public record NewEvent(String name, String venue, Instant startsAt, List<Section> sections) {}
}

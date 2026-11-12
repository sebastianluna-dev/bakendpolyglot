package dev.seatlock.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** "Order" is a JPQL keyword, so the entity is named SeatOrder; the table is still orders. */
@Entity(name = "SeatOrder")
@Table(name = "orders")
public class SeatOrder {
    @Id public UUID id;
    public UUID holdId;
    public String userId;
    public int totalCents;
    public String currency = "USD";
    public String status = "pending";
    public String paymentIntentId;
    public Instant createdAt;
    public Instant updatedAt;
}

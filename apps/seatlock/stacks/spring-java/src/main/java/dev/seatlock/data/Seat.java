package dev.seatlock.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/** Spring Boot's naming strategy maps priceCents to price_cents and rowLabel to row_label. */
@Entity
@Table(name = "seats")
public class Seat {
    @Id public UUID id;
    public UUID eventId;
    public String section;
    public String rowLabel;
    public int number;
    public int priceCents;
    public String status = "available";
    public UUID holdId;
}

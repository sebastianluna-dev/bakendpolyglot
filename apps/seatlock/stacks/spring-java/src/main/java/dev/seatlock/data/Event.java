package dev.seatlock.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** JPA entity over the shared schema (db/migrations owns the tables; ddl-auto is off). */
@Entity
@Table(name = "events")
public class Event {
    @Id public UUID id;
    public String name;
    public String venue;
    public Instant startsAt;
    public String createdBy;
    public Instant createdAt;
}

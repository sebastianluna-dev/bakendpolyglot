package dev.seatlock.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "holds")
public class Hold {
    @Id public UUID id;
    public UUID eventId;
    public String userId;
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "seat_ids", columnDefinition = "uuid[]")
    public UUID[] seatIds;
    public String status = "active";
    public Instant createdAt;
    public Instant expiresAt;
}

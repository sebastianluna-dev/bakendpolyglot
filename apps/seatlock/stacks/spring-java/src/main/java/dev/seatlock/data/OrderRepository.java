package dev.seatlock.data;


import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<SeatOrder, UUID> {

    Optional<SeatOrder> findByHoldId(UUID holdId);

    Optional<SeatOrder> findByIdAndUserId(UUID id, String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from SeatOrder o where o.paymentIntentId = :intentId")
    Optional<SeatOrder> lockByIntent(String intentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from SeatOrder o where o.id = :id and o.paymentIntentId is null")
    Optional<SeatOrder> lockWithoutIntent(UUID id);

    @Modifying
    @Query("update SeatOrder o set o.paymentIntentId = :intentId, o.updatedAt = :now where o.id = :id and o.paymentIntentId is null")
    int attachIntent(UUID id, String intentId, Instant now);
}

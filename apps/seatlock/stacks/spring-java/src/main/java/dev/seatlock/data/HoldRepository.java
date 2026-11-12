package dev.seatlock.data;


import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface HoldRepository extends JpaRepository<Hold, UUID> {

    /** Object-level authorization in the query: someone else's hold does not exist for you. */
    Optional<Hold> findByIdAndUserId(UUID id, String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // SELECT … FOR UPDATE (Chapter 23)
    @Query("select h from Hold h where h.id = :id and h.userId = :userId")
    Optional<Hold> lockOwned(UUID id, String userId);

    @Modifying
    @Query("update Hold h set h.status = 'released' where h.id = :id and h.userId = :userId and h.status = 'active' and h.expiresAt > :now")
    int release(UUID id, String userId, Instant now);

    @Modifying
    @Query("update Hold h set h.status = :status where h.id = :id")
    int setStatus(UUID id, String status);

    /** Expired holds, skipping rows another reaper has locked. */
    @Query(value = """
        SELECT id FROM holds WHERE status = 'active' AND expires_at <= :now
        ORDER BY expires_at LIMIT 1000 FOR UPDATE SKIP LOCKED""", nativeQuery = true)
    List<UUID> lockExpired(Instant now);

    @Modifying
    @Query("update Hold h set h.status = 'expired' where h.id in :ids")
    int expire(Collection<UUID> ids);
}

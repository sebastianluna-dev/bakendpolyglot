package dev.seatlock.data;


import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SeatRepository extends JpaRepository<Seat, UUID> {

    List<Seat> findByEventIdOrderBySectionAscRowLabelAscNumberAsc(UUID eventId);

    List<Seat> findByHoldId(UUID holdId);

    long countByEventId(UUID eventId);

    interface IdStatus {
        UUID getId();
        String getStatus();
    }

    @Query("select s.id as id, s.status as status from Seat s where s.eventId = :eventId")
    List<IdStatus> statuses(UUID eventId);

    @Query("select s.id from Seat s where s.eventId = :eventId and s.id in :ids and s.status = 'available'")
    List<UUID> findFree(UUID eventId, Collection<UUID> ids);

    /** Lock the requested rows in id order first, so overlapping holds cannot deadlock. */
    @Query(value = "SELECT id FROM seats WHERE event_id = :eventId AND id IN (:ids) ORDER BY id FOR UPDATE", nativeQuery = true)
    List<UUID> lockInOrder(UUID eventId, Collection<UUID> ids);

    /** The claim (Chapter 23): one conditional UPDATE whose row count decides. */
    @Modifying
    @Query("""
        update Seat s set s.status = 'held', s.holdId = :holdId
        where s.eventId = :eventId and s.id in :ids and s.status = 'available'""")
    int claim(UUID eventId, Collection<UUID> ids, UUID holdId);

    @Modifying
    @Query("update Seat s set s.status = 'available', s.holdId = null where s.holdId in :holdIds and s.status = 'held'")
    int releaseHeld(Collection<UUID> holdIds);

    @Modifying
    @Query("update Seat s set s.status = 'available', s.holdId = null where s.holdId = :holdId")
    int releaseAll(UUID holdId);

    @Modifying
    @Query("update Seat s set s.status = 'sold' where s.holdId = :holdId")
    int sell(UUID holdId);
}

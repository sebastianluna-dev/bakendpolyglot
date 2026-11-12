package dev.seatlock.service;

import dev.seatlock.data.HoldRepository;
import dev.seatlock.data.OrderRepository;
import dev.seatlock.data.SeatOrder;
import dev.seatlock.domain.DomainException;
import dev.seatlock.domain.HoldStatus;
import jakarta.persistence.EntityManager;
import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CheckoutTx {
    private final EntityManager em;
    private final HoldRepository holds;
    private final OrderRepository orders;
    private final JdbcClient jdbc;
    private final Clock clock;

    public CheckoutTx(EntityManager em, HoldRepository holds, OrderRepository orders, JdbcClient jdbc, Clock clock) {
        this.em = em;
        this.holds = holds;
        this.orders = orders;
        this.jdbc = jdbc;
        this.clock = clock;
    }

    /** Locks the hold, then creates the pending order and its lines — or returns the unfinished one. */
    @Transactional
    public SeatOrder begin(String userId, UUID holdId) {
        var now = clock.instant().truncatedTo(ChronoUnit.MICROS);
        var hold = holds.lockOwned(holdId, userId).orElseThrow(() -> DomainException.notFound("Hold", holdId));
        if (HoldStatus.CHECKED_OUT.equals(hold.status)) {
            var existing = orders.findByHoldId(holdId).orElseThrow();
            if (existing.paymentIntentId != null) throw DomainException.holdNotActive(holdId);
            return existing; // resume: the provider call failed last time
        }
        if (HoldStatus.EXPIRED.equals(HoldStatus.visible(hold.status, hold.expiresAt, now))) throw DomainException.holdExpired(holdId);
        if (!HoldStatus.ACTIVE.equals(hold.status)) throw DomainException.holdNotActive(holdId);

        var order = new SeatOrder();
        order.id = UUID.randomUUID();
        order.holdId = holdId;
        order.userId = userId;
        order.totalCents = jdbc.sql("SELECT coalesce(sum(price_cents), 0)::int FROM seats WHERE hold_id = :id").param("id", holdId).query(Integer.class).single();
        order.createdAt = now;
        order.updatedAt = now;
        em.persist(order);
        hold.status = HoldStatus.CHECKED_OUT; // dirty checking writes it at commit
        em.flush();
        jdbc.sql("INSERT INTO order_lines (order_id, seat_id, price_cents) SELECT :orderId, id, price_cents FROM seats WHERE hold_id = :holdId")
                .param("orderId", order.id).param("holdId", holdId).update();
        return order;
    }

    @Transactional
    public void attachIntent(UUID orderId, String intentId) {
        orders.attachIntent(orderId, intentId, clock.instant());
    }
}

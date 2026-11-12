package dev.seatlock.service;

import dev.seatlock.data.OrderRepository;
import dev.seatlock.data.SeatOrder;
import dev.seatlock.domain.DomainException;
import dev.seatlock.domain.Dtos.OrderDto;
import java.time.Clock;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Checkout: commit a pending order first (CheckoutTx, transactional), then call the provider
 * outside any transaction. A checkout interrupted between the two resumes on retry.
 */
@Service
public class CheckoutService {
    private final CheckoutTx tx;
    private final PaymentsClient payments;
    private final OrderRepository orders;
    private final JdbcClient jdbc;
    private final Clock clock;

    public CheckoutService(CheckoutTx tx, PaymentsClient payments, OrderRepository orders, JdbcClient jdbc, Clock clock) {
        this.tx = tx;
        this.payments = payments;
        this.orders = orders;
        this.jdbc = jdbc;
        this.clock = clock;
    }

    public OrderDto checkout(String userId, UUID holdId) {
        SeatOrder order = tx.begin(userId, holdId);
        String intent = payments.createIntent(order.id, order.totalCents, order.currency);
        tx.attachIntent(order.id, intent);
        return find(userId, order.id);
    }

    @Transactional(readOnly = true)
    public OrderDto find(String userId, UUID id) {
        var o = orders.findByIdAndUserId(id, userId).orElseThrow(() -> DomainException.notFound("Order", id));
        var eventId = jdbc.sql("SELECT event_id FROM holds WHERE id = :id").param("id", o.holdId).query(UUID.class).single();
        var seatIds = jdbc.sql("SELECT seat_id::text FROM order_lines WHERE order_id = :id ORDER BY seat_id")
                .param("id", o.id).query(String.class).list();
        return new OrderDto(o.id, o.holdId, eventId, seatIds, o.status, o.totalCents, o.currency, o.paymentIntentId);
    }
}

package dev.seatlock.service;

import dev.seatlock.data.HoldRepository;
import dev.seatlock.data.OrderRepository;
import dev.seatlock.data.SeatRepository;
import dev.seatlock.domain.HoldStatus;
import java.time.Clock;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Applies verified payment events: dedupe by event id, then change only pending orders (Chapter 24). */
@Service
public class PaymentEvents {
    public record PaymentEvent(String id, String type, String paymentIntentId, UUID orderId) {}

    private static final Logger log = LoggerFactory.getLogger(PaymentEvents.class);
    private final JdbcClient jdbc;
    private final OrderRepository orders;
    private final SeatRepository seats;
    private final HoldRepository holds;
    private final Clock clock;

    public PaymentEvents(JdbcClient jdbc, OrderRepository orders, SeatRepository seats, HoldRepository holds, Clock clock) {
        this.jdbc = jdbc;
        this.orders = orders;
        this.seats = seats;
        this.holds = holds;
        this.clock = clock;
    }

    @Transactional
    public void apply(PaymentEvent e) {
        int recorded = jdbc.sql("INSERT INTO webhook_events (id, type) VALUES (:id, :type) ON CONFLICT DO NOTHING")
                .param("id", e.id()).param("type", e.type()).update();
        if (recorded == 0) return; // duplicate delivery

        var order = orders.lockByIntent(e.paymentIntentId())
                // The webhook can beat us to saving the intent id; the provider echoes our order id.
                .or(() -> e.orderId() == null ? java.util.Optional.empty() : orders.lockWithoutIntent(e.orderId()))
                .orElse(null);
        if (order == null || !"pending".equals(order.status)) {
            log.info("payment event {} ignored (order {})", e.id(), order == null ? "unknown" : order.status);
            return; // late or out-of-order events never undo a final state
        }
        if ("payment_intent.succeeded".equals(e.type())) {
            order.status = "paid";
            seats.sell(order.holdId);
        } else {
            order.status = "failed";
            seats.releaseAll(order.holdId);
            holds.setStatus(order.holdId, HoldStatus.RELEASED);
        }
        order.paymentIntentId = e.paymentIntentId();
        order.updatedAt = clock.instant();
    }
}

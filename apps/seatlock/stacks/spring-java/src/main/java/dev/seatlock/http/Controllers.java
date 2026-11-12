package dev.seatlock.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.seatlock.AppConfig;
import dev.seatlock.data.SeatRepository;
import dev.seatlock.domain.DomainException;
import dev.seatlock.domain.Dtos.EventDto;
import dev.seatlock.domain.Dtos.HoldDto;
import dev.seatlock.domain.Dtos.NewEvent;
import dev.seatlock.domain.Dtos.OrderDto;
import dev.seatlock.domain.Dtos.Section;
import dev.seatlock.domain.Dtos.SeatMapDto;
import dev.seatlock.service.CheckoutService;
import dev.seatlock.service.EventService;
import dev.seatlock.service.HoldService;
import dev.seatlock.service.Idempotency;
import dev.seatlock.service.PaymentEvents;
import dev.seatlock.service.PaymentEvents.PaymentEvent;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * The handler layer (Chapter 18). Ids arrive as Strings and bodies as raw text, so a bad id or
 * malformed JSON becomes SeatLock's own problem. Roles are checked in SecurityConfig.
 */
@RestController
class Controllers {
    private static final int MAX_SEATS_PER_EVENT = 5000;

    private final EventService events;
    private final SeatRepository seats;
    private final HoldService holds;
    private final CheckoutService checkout;
    private final Idempotency idempotency;
    private final PaymentEvents paymentEvents;
    private final AppConfig config;
    private final ObjectMapper json;

    Controllers(EventService events, SeatRepository seats, HoldService holds, CheckoutService checkout, Idempotency idempotency,
                PaymentEvents paymentEvents, AppConfig config, ObjectMapper json) {
        this.events = events;
        this.seats = seats;
        this.holds = holds;
        this.checkout = checkout;
        this.idempotency = idempotency;
        this.paymentEvents = paymentEvents;
        this.config = config;
        this.json = json;
    }

    @GetMapping("/health")
    Map<String, String> health() {
        return Map.of("status", "ok");
    }

    // ---------- events ----------

    @PostMapping("/events")
    ResponseEntity<EventDto> createEvent(@RequestBody(required = false) String raw, @AuthenticationPrincipal Jwt jwt) {
        var body = Body.read(json, raw, "name", "venue", "startsAt", "sections");
        var name = body.str("name", 1, 200);
        var venue = body.str("venue", 1, 200);
        var startsAt = body.dateTime("startsAt");
        var sections = body.objects("sections", 1, 10, "name", "rows", "seatsPerRow", "priceCents").stream()
                .map(s -> new Section(s.str("name", 1, 40), s.integer("rows", 1, 26), s.integer("seatsPerRow", 1, 50), s.integer("priceCents", 0, 1_000_000)))
                .toList();
        if (new HashSet<>(sections.stream().map(Section::name).toList()).size() != sections.size()) body.fail("sections", "section names must be unique");
        if (sections.stream().mapToInt(s -> s.rows() * s.seatsPerRow()).sum() > MAX_SEATS_PER_EVENT) {
            body.fail("sections", "at most " + MAX_SEATS_PER_EVENT + " seats per event");
        }
        body.throwIfInvalid();

        var event = events.create(new NewEvent(name, venue, startsAt, sections), jwt.getSubject());
        return ResponseEntity.created(URI.create("/events/" + event.id())).body(event);
    }

    @GetMapping("/events/{id}")
    ResponseEntity<EventDto> getEvent(@PathVariable String id) {
        return ResponseEntity.ok().cacheControl(CacheControl.maxAge(java.time.Duration.ofSeconds(60)).cachePublic()).body(events.get(Body.pathId(id)));
    }

    /** The layout may come from the cache; availability never does. */
    @GetMapping("/events/{id}/seats")
    ResponseEntity<SeatMapDto> seatMap(@PathVariable String id) {
        var eventId = Body.pathId(id);
        var layout = events.layout(eventId);
        var status = new HashMap<UUID, String>();
        for (var s : seats.statuses(eventId)) status.put(s.getId(), s.getStatus());
        var items = layout.stream().map(s -> s.withStatus(status.get(s.id()))).toList();
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(new SeatMapDto(eventId, items));
    }

    // ---------- holds and orders ----------

    @PostMapping("/holds")
    ResponseEntity<HoldDto> createHold(@RequestBody(required = false) String raw, @AuthenticationPrincipal Jwt jwt) {
        var body = Body.read(json, raw, "eventId", "seatIds");
        var eventId = body.uuid("eventId");
        var seatIds = body.uuids("seatIds", config.maxSeatsPerHold());
        body.throwIfInvalid();

        var hold = holds.create(jwt.getSubject(), eventId, seatIds);
        return ResponseEntity.created(URI.create("/holds/" + hold.id())).body(hold);
    }

    @GetMapping("/holds/{id}")
    HoldDto getHold(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return holds.get(jwt.getSubject(), Body.pathId(id));
    }

    @DeleteMapping("/holds/{id}")
    ResponseEntity<Void> releaseHold(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        holds.release(jwt.getSubject(), Body.pathId(id));
        return ResponseEntity.noContent().build();
    }

    /** Checkout with an Idempotency-Key (Chapter 24); the stored outcome decides the status. */
    @PostMapping("/holds/{id}/checkout")
    ResponseEntity<String> checkout(@PathVariable String id, @RequestHeader(value = "Idempotency-Key", required = false) String key,
                                    @AuthenticationPrincipal Jwt jwt, HttpServletRequest req) throws Exception {
        var holdId = Body.pathId(id);
        if (key == null || key.isEmpty() || key.length() > 200) throw DomainException.idempotencyKeyRequired();
        var user = jwt.getSubject();
        var outcome = idempotency.runOnce(user, key, "POST /holds/" + holdId + "/checkout", req.getRequestURI(),
                () -> checkout.checkout(user, holdId), 201);

        var res = ResponseEntity.status(outcome.status())
                .contentType(outcome.status() >= 400 ? MediaType.APPLICATION_PROBLEM_JSON : MediaType.APPLICATION_JSON);
        if (outcome.replayed()) res.header("Idempotent-Replayed", "true");
        if (outcome.status() == 201) res.header(HttpHeaders.LOCATION, "/orders/" + json.readTree(outcome.body()).get("id").asText());
        return res.body(outcome.body());
    }

    @GetMapping("/orders/{id}")
    OrderDto getOrder(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return checkout.find(jwt.getSubject(), Body.pathId(id));
    }

    /** The reaper on demand. The hold-reaper client's service account has box-office. */
    @PostMapping("/admin/holds/release-expired")
    Map<String, Integer> runReaper() {
        return Map.of("released", holds.releaseExpired());
    }

    // ---------- payment webhooks: verify the raw bytes, then dedupe and apply (Chapter 24) ----------

    static boolean signatureMatches(byte[] raw, String given, String secret) {
        try {
            var mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            var expected = HexFormat.of().formatHex(mac.doFinal(raw)).getBytes(StandardCharsets.US_ASCII);
            return MessageDigest.isEqual(expected, (given == null ? "" : given).getBytes(StandardCharsets.US_ASCII)); // constant time
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    @PostMapping("/webhooks/payments")
    ResponseEntity<Void> paymentWebhook(@RequestBody(required = false) byte[] raw, @RequestHeader(value = "X-Signature", required = false) String signature) {
        var bytes = raw == null ? new byte[0] : raw;
        if (!signatureMatches(bytes, signature, config.webhookSecret())) throw DomainException.invalidSignature();
        PaymentEvent event;
        try {
            var root = json.readTree(bytes);
            var data = root.get("data");
            var eventId = root.get("id").textValue();
            var type = root.get("type").textValue();
            var intent = data.get("paymentIntentId").textValue();
            if (eventId == null || type == null || intent == null || eventId.isEmpty() || type.isEmpty() || intent.isEmpty()) throw new IllegalArgumentException();
            var orderNode = data.get("orderId");
            UUID orderId = null;
            if (orderNode != null && orderNode.isTextual()) {
                try {
                    orderId = UUID.fromString(orderNode.textValue());
                } catch (IllegalArgumentException ignored) {
                    // not ours
                }
            }
            event = new PaymentEvent(eventId, type, intent, orderId);
        } catch (Exception e) {
            throw DomainException.invalidBody();
        }
        if (event.type().equals("payment_intent.succeeded") || event.type().equals("payment_intent.failed")) paymentEvents.apply(event);
        return ResponseEntity.noContent().build();
    }
}

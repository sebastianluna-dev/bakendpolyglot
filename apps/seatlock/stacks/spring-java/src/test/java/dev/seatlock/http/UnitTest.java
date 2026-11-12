package dev.seatlock.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.seatlock.AppConfig;
import dev.seatlock.domain.DomainException;
import dev.seatlock.domain.HoldStatus;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Test;

/** Fast tests: no Spring context, no database, no identity provider. */
class UnitTest {
    private final ObjectMapper json = new ObjectMapper();

    @Test
    @SuppressWarnings("unchecked")
    void bodyReportsEveryInvalidFieldWithNestedPaths() {
        var body = Body.read(json, "{\"name\":\"\",\"sections\":[{\"name\":\"A\",\"rows\":27,\"x\":1}]}", "name", "sections");
        body.str("name", 1, 200);
        for (var s : body.objects("sections", 1, 10, "name", "rows", "seatsPerRow", "priceCents")) s.integer("rows", 1, 26);
        var e = assertThrows(DomainException.class, body::throwIfInvalid);
        var errors = (Map<String, List<String>>) e.extensions().get("errors");
        assertTrue(errors.containsKey("name"));
        assertTrue(errors.containsKey("sections[0].rows"));
        assertTrue(errors.containsKey("sections[0].x"));
    }

    @Test
    void malformedJsonIsInvalidBody() {
        assertEquals("invalid-body", assertThrows(DomainException.class, () -> Body.read(json, "{\"name\": ")).code());
    }

    @Test
    void anActiveHoldPastExpiryReadsAsExpired() {
        var now = Instant.parse("2026-03-01T10:00:00Z");
        assertEquals(HoldStatus.EXPIRED, HoldStatus.visible(HoldStatus.ACTIVE, now.minusSeconds(1), now));
        assertEquals(HoldStatus.ACTIVE, HoldStatus.visible(HoldStatus.ACTIVE, now.plusSeconds(60), now));
        assertEquals(HoldStatus.CHECKED_OUT, HoldStatus.visible(HoldStatus.CHECKED_OUT, now.minusSeconds(3600), now));
    }

    @Test
    void webhookSignatureIsTheHmacOfTheRawBytes() throws Exception {
        var raw = "{\"id\":\"evt_1\"}".getBytes(StandardCharsets.UTF_8);
        var mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec("secret".getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        var good = HexFormat.of().formatHex(mac.doFinal(raw));
        assertTrue(Controllers.signatureMatches(raw, good, "secret"));
        assertFalse(Controllers.signatureMatches(raw, good, "other"));
        assertFalse(Controllers.signatureMatches("{\"id\": \"evt_1\"}".getBytes(StandardCharsets.UTF_8), good, "secret"));
        assertFalse(Controllers.signatureMatches(raw, null, "secret"));
    }

    @Test
    void configReportsEveryProblemAtOnce() {
        var e = assertThrows(IllegalStateException.class, () -> AppConfig.load(k -> k.equals("HOLD_TTL_SECONDS") ? "0" : null));
        assertTrue(e.getMessage().contains("DATABASE_URL"));
        assertTrue(e.getMessage().contains("HOLD_TTL_SECONDS"));
    }
}

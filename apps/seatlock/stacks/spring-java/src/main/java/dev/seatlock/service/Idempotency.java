package dev.seatlock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.seatlock.domain.DomainException;
import dev.seatlock.http.Problems;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.function.Supplier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

/** Idempotency keys (Chapter 24). The primary key on (user_id, key) makes two retries unable to both run. */
@Service
public class Idempotency {
    public record Outcome(int status, String body, boolean replayed) {}

    private record Stored(String requestHash, Integer status, String response) {}

    private final JdbcClient jdbc;
    private final ObjectMapper json;

    public Idempotency(JdbcClient jdbc, ObjectMapper json) {
        this.jdbc = jdbc;
        this.json = json;
    }

    /** 4xx outcomes are stored like successes; 5xx are not, so a retry after an outage runs again. */
    public Outcome runOnce(String userId, String key, String fingerprint, String instance, Supplier<Object> operation, int okStatus) {
        var hash = sha256(fingerprint);
        int claimed = jdbc.sql("INSERT INTO idempotency_keys (key, user_id, request_hash) VALUES (:key, :user, :hash) ON CONFLICT DO NOTHING")
                .param("key", key).param("user", userId).param("hash", hash).update();
        if (claimed == 0) {
            var stored = jdbc.sql("SELECT request_hash, status, response::text FROM idempotency_keys WHERE user_id = :user AND key = :key")
                    .param("user", userId).param("key", key)
                    .query((rs, n) -> new Stored(rs.getString(1), (Integer) rs.getObject(2), rs.getString(3))).single();
            if (!stored.requestHash().equals(hash)) throw DomainException.idempotencyKeyReuse();
            if (stored.status() == null) throw DomainException.idempotencyInProgress();
            return new Outcome(stored.status(), stored.response(), true);
        }

        int status;
        String body;
        try {
            body = write(operation.get());
            status = okStatus;
        } catch (DomainException e) {
            if (e.status() >= 500) {
                forget(userId, key);
                throw e;
            }
            status = e.status();
            body = write(Problems.body(e, instance));
        } catch (RuntimeException e) {
            forget(userId, key);
            throw e;
        }
        jdbc.sql("UPDATE idempotency_keys SET status = :status, response = :body::jsonb WHERE user_id = :user AND key = :key")
                .param("status", status).param("body", body).param("user", userId).param("key", key).update();
        return new Outcome(status, body, false);
    }

    private void forget(String userId, String key) {
        jdbc.sql("DELETE FROM idempotency_keys WHERE user_id = :user AND key = :key").param("user", userId).param("key", key).update();
    }

    private String write(Object value) {
        try {
            return json.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String sha256(String s) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}

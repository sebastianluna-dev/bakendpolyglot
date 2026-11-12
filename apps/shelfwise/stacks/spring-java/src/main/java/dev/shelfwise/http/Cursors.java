package dev.shelfwise.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shelfwise.domain.DomainException;
import java.util.Base64;
import java.util.function.Predicate;

/** Opaque base64url cursors (Chapter 12). */
final class Cursors {
    private Cursors() {}

    static String encode(ObjectMapper json, Object value) {
        try {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(json.writeValueAsBytes(value));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    static <T> T decode(ObjectMapper json, String raw, Class<T> type, Predicate<T> valid) {
        try {
            T value = json.readValue(Base64.getUrlDecoder().decode(raw), type);
            if (value != null && valid.test(value)) return value;
        } catch (Exception e) {
            // fall through: any decoding failure is a bad cursor
        }
        throw DomainException.badCursor();
    }
}

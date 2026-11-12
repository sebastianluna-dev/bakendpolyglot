package dev.shelfwise.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shelfwise.domain.DomainException;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A JSON request body read field by field, so we can reject unknown fields and report every invalid
 * field at once (Chapter 13). Bean Validation on a DTO can't tell "absent" from "null" in a PATCH.
 */
final class Body {
    static final Pattern ISBN13 = Pattern.compile("^97[89][0-9]{10}$");
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+$");

    private final Map<String, JsonNode> fields = new LinkedHashMap<>();
    private final Map<String, List<String>> errors = new LinkedHashMap<>();

    static Body read(ObjectMapper json, String raw, String... allowed) {
        JsonNode root;
        try {
            root = raw == null || raw.isBlank() ? null : json.readTree(raw);
        } catch (JsonProcessingException e) {
            root = null;
        }
        if (root == null) throw DomainException.invalidBody();

        var body = new Body();
        if (!root.isObject()) {
            body.fail("body", "must be a JSON object");
            return body;
        }
        var allow = Set.of(allowed);
        for (var e : root.properties()) {
            body.fields.put(e.getKey(), e.getValue());
            if (!allow.contains(e.getKey())) body.fail(e.getKey(), "is not allowed");
        }
        return body;
    }

    void fail(String field, String message) {
        errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }

    boolean has(String field) { return fields.containsKey(field); }

    int size() { return fields.size(); }

    /** Throws a 422 validation problem if any field failed. */
    void throwIfInvalid() {
        if (!errors.isEmpty()) throw DomainException.validation(errors);
    }

    String str(String field, boolean required, int min, int max) {
        return str(field, required, min, max, null, "");
    }

    String str(String field, boolean required, int min, int max, Pattern pattern, String patternMessage) {
        var node = fields.get(field);
        if (node == null) {
            if (required) fail(field, "is required");
            return null;
        }
        if (!node.isTextual()) {
            fail(field, "must be a string");
            return null;
        }
        var s = node.textValue();
        int length = s.codePointCount(0, s.length());
        if (length < min || length > max) {
            fail(field, "must be " + min + "–" + max + " characters");
            return null;
        }
        if (pattern != null && !pattern.matcher(s).matches()) {
            fail(field, patternMessage);
            return null;
        }
        return s;
    }

    /** An integer within [min, max]; nullable fields may be JSON null. */
    Long integer(String field, boolean required, boolean nullable, long min, long max, String maxMessage) {
        var node = fields.get(field);
        if (node == null) {
            if (required) fail(field, "is required");
            return null;
        }
        if (node.isNull()) {
            if (!nullable) fail(field, "cannot be null");
            return null;
        }
        if (!node.isIntegralNumber() || !node.canConvertToLong()) {
            fail(field, "must be an integer");
            return null;
        }
        long n = node.longValue();
        if (n < min) {
            fail(field, "must be at least " + min);
            return null;
        }
        if (n > max) {
            fail(field, maxMessage);
            return null;
        }
        return n;
    }

    String email(String field) {
        var s = str(field, true, 3, 254);
        if (s != null && !EMAIL.matcher(s).matches()) {
            fail(field, "must be a valid email address");
            return null;
        }
        return s;
    }

    static long thisYear() { return Year.now().getValue(); }
}

package dev.seatlock.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.seatlock.domain.DomainException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * A JSON object read field by field, so we can reject unknown fields and report every invalid
 * field at once (Chapter 13). Nested objects share the parent's error map under a path prefix.
 */
public final class Body {
    private static final Pattern UUID_RE = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private final Map<String, JsonNode> fields = new LinkedHashMap<>();
    private final Map<String, List<String>> errors;
    private final String path;

    private Body(Map<String, List<String>> errors, String path) {
        this.errors = errors;
        this.path = path;
    }

    public static Body read(ObjectMapper json, String raw, String... allowed) {
        JsonNode root;
        try {
            root = raw == null || raw.isBlank() ? null : json.readTree(raw);
        } catch (JsonProcessingException e) {
            root = null;
        }
        if (root == null) throw DomainException.invalidBody();
        var body = new Body(new LinkedHashMap<>(), "");
        body.load(root, allowed);
        return body;
    }

    private void load(JsonNode root, String[] allowed) {
        if (!root.isObject()) {
            fail("", "must be a JSON object");
            return;
        }
        var allow = Set.of(allowed);
        for (var e : root.properties()) {
            fields.put(e.getKey(), e.getValue());
            if (!allow.contains(e.getKey())) fail(e.getKey(), "is not allowed");
        }
    }

    public void fail(String field, String message) {
        var name = (path + field).replaceAll("\\.$", "");
        errors.computeIfAbsent(name.isEmpty() ? "body" : name, k -> new ArrayList<>()).add(message);
    }

    public void throwIfInvalid() {
        if (!errors.isEmpty()) throw DomainException.validation(errors);
    }

    private JsonNode need(String field) {
        var node = fields.get(field);
        if (node == null) fail(field, "is required");
        return node;
    }

    public String str(String field, int min, int max) {
        var node = need(field);
        if (node == null) return "";
        if (!node.isTextual()) {
            fail(field, "must be a string");
            return "";
        }
        var s = node.textValue();
        int length = s.codePointCount(0, s.length());
        if (length < min || length > max) fail(field, "must be " + min + "–" + max + " characters");
        return s;
    }

    public int integer(String field, int min, int max) {
        var node = need(field);
        if (node == null) return 0;
        if (!node.isIntegralNumber() || !node.canConvertToInt()) {
            fail(field, "must be an integer");
            return 0;
        }
        int n = node.intValue();
        if (n < min || n > max) fail(field, "must be between " + min + " and " + max);
        return n;
    }

    public Instant dateTime(String field) {
        var s = str(field, 1, 64);
        if (s.isEmpty()) return null;
        try {
            return OffsetDateTime.parse(s).toInstant();
        } catch (DateTimeParseException e) {
            fail(field, "must be an RFC 3339 date-time");
            return null;
        }
    }

    public UUID uuid(String field) {
        var s = str(field, 1, 64);
        if (!s.isEmpty() && UUID_RE.matcher(s).matches()) return UUID.fromString(s);
        if (!s.isEmpty()) fail(field, "must be a UUID");
        return null;
    }

    /** A non-empty array of distinct UUIDs. */
    public List<UUID> uuids(String field, int max) {
        var out = new ArrayList<UUID>();
        var node = need(field);
        if (node == null) return out;
        if (!node.isArray()) {
            fail(field, "must be an array of UUIDs");
            return out;
        }
        for (int i = 0; i < node.size(); i++) {
            var item = node.get(i);
            if (item.isTextual() && UUID_RE.matcher(item.textValue()).matches()) out.add(UUID.fromString(item.textValue()));
            else fail(field + "[" + i + "]", "must be a UUID");
        }
        if (new HashSet<>(out).size() != out.size()) fail(field, "seats must be unique");
        if (node.isEmpty() || node.size() > max) fail(field, "must hold 1 to " + max + " seats");
        return out;
    }

    /** An array of nested objects, validated into the same error map. */
    public List<Body> objects(String field, int min, int max, String... allowed) {
        var out = new ArrayList<Body>();
        var node = need(field);
        if (node == null) return out;
        if (!node.isArray()) {
            fail(field, "must be an array");
            return out;
        }
        for (int i = 0; i < node.size(); i++) {
            var child = new Body(errors, path + field + "[" + i + "].");
            child.load(node.get(i), allowed);
            out.add(child);
        }
        if (node.size() < min || node.size() > max) fail(field, "must have " + min + " to " + max + " items");
        return out;
    }

    public static UUID pathId(String raw) {
        if (!UUID_RE.matcher(raw).matches()) throw DomainException.invalidId(raw);
        return UUID.fromString(raw);
    }
}

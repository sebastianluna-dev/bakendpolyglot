package dev.shelfwise.http;

import dev.shelfwise.domain.DomainException;
import dev.shelfwise.domain.Models.Book;
import dev.shelfwise.domain.Models.Page;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/** Small helpers shared by the controllers. */
final class Http {
    private Http() {}

    private static final Pattern DIGITS = Pattern.compile("^[0-9]{1,18}$");
    private static final Pattern IF_MATCH = Pattern.compile("^\"book-(\\d+)-v(\\d+)\"$");

    static long parseId(String raw) {
        if (DIGITS.matcher(raw).matches()) {
            long id = Long.parseLong(raw);
            if (id >= 1) return id;
        }
        throw DomainException.invalidId(raw);
    }

    static int parseLimit(String raw) {
        if (raw == null || raw.isEmpty()) return 20;
        try {
            int n = Integer.parseInt(raw);
            if (n >= 1 && n <= 100) return n;
        } catch (NumberFormatException ignored) {
            // reported below
        }
        throw DomainException.validation("limit", "must be an integer between 1 and 100");
    }

    static String etag(Book b) {
        return "\"book-" + b.id() + "-v" + b.version() + "\"";
    }

    /** Parses If-Match; a header naming another book never matches (-1). */
    static Integer expectedVersion(String header, long id) {
        if (header == null || header.isEmpty()) return null;
        var m = IF_MATCH.matcher(header.trim());
        if (m.matches() && m.group(1).equals(Long.toString(id))) {
            try {
                return Integer.parseInt(m.group(2));
            } catch (NumberFormatException ignored) {
                return -1;
            }
        }
        return -1;
    }

    /** Trims the extra row fetched to detect a next page and builds the cursor from the last item. */
    static <T> Page<T> page(List<T> rows, int limit, Function<T, String> cursorOf) {
        if (rows.size() <= limit) return new Page<>(rows, null, limit);
        var items = rows.subList(0, limit);
        return new Page<>(List.copyOf(items), cursorOf.apply(items.get(limit - 1)), limit);
    }
}

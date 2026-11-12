package dev.shelfwise.domain;

import java.time.Instant;
import java.util.List;

/** Records (Java 16+): immutable carriers; Jackson serializes their components by name. */
public final class Models {
    private Models() {}

    public record Book(long id, String isbn, String title, String author, Integer year, int version) {}

    public record Copy(long id, long bookId, String barcode, String status) {}

    public record Member(long id, String email, String name) {}

    public record Loan(long id, long copyId, long memberId, long bookId, String title, String barcode,
                       Instant loanedAt, Instant dueAt, Instant returnedAt) {}

    /** The shape of every paginated list (Chapter 12). */
    public record Page<T>(List<T> items, String nextCursor, int limit) {}

    public record NewBook(String isbn, String title, String author, Integer year) {}

    /** Only the fields present in a PATCH; yearSet tells "set to null" apart from "absent". */
    public record BookChanges(String title, String author, Integer year, boolean yearSet) {}

    public record BookCursor(String title, long id) {}

    public record LoanCursor(long id) {}
}

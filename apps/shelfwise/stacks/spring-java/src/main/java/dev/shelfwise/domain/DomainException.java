package dev.shelfwise.domain;

import java.util.List;
import java.util.Map;

/** A domain error; the HTTP layer renders it as Problem Details (Chapter 12). Unchecked, as Spring expects. */
public final class DomainException extends RuntimeException {
    private final String code;
    private final int status;
    private final String title;
    private final Map<String, Object> extensions;

    public DomainException(String code, int status, String title, String detail, Map<String, Object> extensions) {
        super(detail);
        this.code = code;
        this.status = status;
        this.title = title;
        this.extensions = extensions;
    }

    private DomainException(String code, int status, String title, String detail) {
        this(code, status, title, detail, Map.of());
    }

    public String code() { return code; }
    public int status() { return status; }
    public String title() { return title; }
    public Map<String, Object> extensions() { return extensions; }

    public static DomainException notFound(String what, Object id) {
        return new DomainException("not-found", 404, "Resource not found", what + " " + id + " does not exist.");
    }

    public static DomainException duplicateIsbn(String isbn) {
        return new DomainException("duplicate-isbn", 409, "ISBN already in the catalog",
                "A book with ISBN " + isbn + " already exists.", Map.of("isbn", isbn));
    }

    public static DomainException duplicateBarcode(String barcode) {
        return new DomainException("duplicate-barcode", 409, "Barcode already in use",
                "A copy with barcode " + barcode + " already exists.", Map.of("barcode", barcode));
    }

    public static DomainException duplicateEmail(String email) {
        return new DomainException("duplicate-email", 409, "Email already registered",
                "A member with email " + email + " already exists.");
    }

    public static DomainException copyOnLoan(long copyId) {
        return new DomainException("copy-on-loan", 409, "Copy is already on loan",
                "Copy " + copyId + " has an open loan.", Map.of("copyId", copyId));
    }

    public static DomainException loanLimit(long memberId, int max) {
        return new DomainException("loan-limit", 409, "Loan limit reached",
                "Member " + memberId + " already has " + max + " open loans.", Map.of("memberId", memberId, "max", max));
    }

    public static DomainException alreadyReturned(long loanId) {
        return new DomainException("already-returned", 409, "Loan already returned", "Loan " + loanId + " was already returned.");
    }

    public static DomainException versionMismatch(long bookId) {
        return new DomainException("version-mismatch", 412, "Book was changed by someone else",
                "Book " + bookId + " does not match the If-Match version.");
    }

    public static DomainException badCursor() {
        return new DomainException("bad-cursor", 400, "Invalid cursor", "The cursor is malformed or expired; start from the first page.");
    }

    public static DomainException invalidId(String raw) {
        return new DomainException("invalid-id", 400, "Invalid id", "\"" + raw + "\" is not a valid id.");
    }

    public static DomainException invalidBody() {
        return new DomainException("invalid-body", 400, "Malformed JSON", "The request body is not valid JSON.");
    }

    public static DomainException validation(Map<String, List<String>> errors) {
        return new DomainException("validation", 422, "Request is invalid", "One or more fields are invalid.", Map.of("errors", errors));
    }

    public static DomainException validation(String field, String message) {
        return validation(Map.of(field, List.of(message)));
    }

    public static DomainException internal(String requestId) {
        return new DomainException("internal", 500, "Internal error", "Something went wrong. Quote request id " + requestId + ".");
    }
}

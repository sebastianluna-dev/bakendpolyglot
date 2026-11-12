namespace Shelfwise.Domain;

/// <summary>A domain error; the HTTP layer renders it as Problem Details (Chapter 12).</summary>
public sealed class DomainException(
    string code, int status, string title, string detail,
    IReadOnlyDictionary<string, object?>? extensions = null) : Exception(detail)
{
    public string Code { get; } = code;
    public int Status { get; } = status;
    public string Title { get; } = title;
    public IReadOnlyDictionary<string, object?> Extensions { get; } = extensions ?? new Dictionary<string, object?>();

    public static DomainException NotFound(string what, object id) =>
        new("not-found", 404, "Resource not found", $"{what} {id} does not exist.");

    public static DomainException DuplicateIsbn(string isbn) =>
        new("duplicate-isbn", 409, "ISBN already in the catalog", $"A book with ISBN {isbn} already exists.",
            new Dictionary<string, object?> { ["isbn"] = isbn });

    public static DomainException DuplicateBarcode(string barcode) =>
        new("duplicate-barcode", 409, "Barcode already in use", $"A copy with barcode {barcode} already exists.",
            new Dictionary<string, object?> { ["barcode"] = barcode });

    public static DomainException DuplicateEmail(string email) =>
        new("duplicate-email", 409, "Email already registered", $"A member with email {email} already exists.");

    public static DomainException CopyOnLoan(long copyId) =>
        new("copy-on-loan", 409, "Copy is already on loan", $"Copy {copyId} has an open loan.",
            new Dictionary<string, object?> { ["copyId"] = copyId });

    public static DomainException LoanLimit(long memberId, int max) =>
        new("loan-limit", 409, "Loan limit reached", $"Member {memberId} already has {max} open loans.",
            new Dictionary<string, object?> { ["memberId"] = memberId, ["max"] = max });

    public static DomainException AlreadyReturned(long loanId) =>
        new("already-returned", 409, "Loan already returned", $"Loan {loanId} was already returned.");

    public static DomainException VersionMismatch(long bookId) =>
        new("version-mismatch", 412, "Book was changed by someone else", $"Book {bookId} does not match the If-Match version.");

    public static DomainException BadCursor() =>
        new("bad-cursor", 400, "Invalid cursor", "The cursor is malformed or expired; start from the first page.");

    public static DomainException InvalidId(string raw) =>
        new("invalid-id", 400, "Invalid id", $"\"{raw}\" is not a valid id.");

    public static DomainException InvalidBody() =>
        new("invalid-body", 400, "Malformed JSON", "The request body is not valid JSON.");

    public static DomainException Validation(IReadOnlyDictionary<string, List<string>> errors) =>
        new("validation", 422, "Request is invalid", "One or more fields are invalid.",
            new Dictionary<string, object?> { ["errors"] = errors });

    public static DomainException Internal(string requestId) =>
        new("internal", 500, "Internal error", $"Something went wrong. Quote request id {requestId}.");
}

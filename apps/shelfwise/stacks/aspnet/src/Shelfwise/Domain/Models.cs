namespace Shelfwise.Domain;

// Records: immutable, value equality, and System.Text.Json serializes them as camelCase objects.
public sealed record Book(long Id, string Isbn, string Title, string Author, int? Year, int Version);

public sealed record Copy(long Id, long BookId, string Barcode, string Status);

public sealed record Member(long Id, string Email, string Name);

public sealed record Loan(
    long Id, long CopyId, long MemberId, long BookId, string Title, string Barcode,
    DateTimeOffset LoanedAt, DateTimeOffset DueAt, DateTimeOffset? ReturnedAt);

/// <summary>The shape of every paginated list (Chapter 12).</summary>
public sealed record Page<T>(IReadOnlyList<T> Items, string? NextCursor, int Limit);

public sealed record NewBook(string Isbn, string Title, string Author, int? Year);

/// <summary>Only the fields present in a PATCH. YearSet tells "set to null" apart from "absent".</summary>
public sealed record BookChanges(string? Title, string? Author, int? Year, bool YearSet);

public sealed record BookCursor(string Title, long Id);

public sealed record LoanCursor(long Id);

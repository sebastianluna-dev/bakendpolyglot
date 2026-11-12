using System.Text.RegularExpressions;
using Shelfwise.Data;
using Shelfwise.Domain;

namespace Shelfwise.Http;

/// <summary>
/// The handler layer: parse, validate, call a repository or service, shape the response (Chapter 18).
/// Route ids are bound as strings so a bad id is our 400 invalid-id, not the framework's own 400.
/// </summary>
public static partial class Endpoints
{
    public static void Map(WebApplication app)
    {
        app.MapGet("/health", () => Results.Json(new { status = "ok" }));
        app.MapGet("/books", ListBooks);
        app.MapPost("/books", CreateBook);
        app.MapGet("/books/{id}", GetBook);
        app.MapPatch("/books/{id}", UpdateBook);
        app.MapPost("/books/{id}/copies", AddCopy);
        app.MapPost("/members", CreateMember);
        app.MapGet("/members/{id}/loans", MemberLoans);
        app.MapPost("/loans", Lend);
        app.MapPost("/loans/{id}/return", ReturnLoan);
        app.MapFallback(new RequestDelegate(ctx => throw DomainException.NotFound("Route", ctx.Request.Path.Value ?? "/")));
    }

    static long ParseId(string raw) =>
        raw.Length > 0 && raw.All(char.IsAsciiDigit) && long.TryParse(raw, out var id) && id >= 1
            ? id : throw DomainException.InvalidId(raw);

    static int ParseLimit(HttpRequest req)
    {
        var raw = req.Query["limit"].ToString();
        if (raw == "") return 20;
        return int.TryParse(raw, out var n) && n is >= 1 and <= 100
            ? n
            : throw DomainException.Validation(new Dictionary<string, List<string>> { ["limit"] = ["must be an integer between 1 and 100"] });
    }

    static string ETag(Book b) => $"\"book-{b.Id}-v{b.Version}\"";

    [GeneratedRegex("^\"book-(\\d+)-v(\\d+)\"$")]
    private static partial Regex IfMatch();

    /// <summary>Parses If-Match; a header naming another book never matches (-1).</summary>
    static int? ExpectedVersion(string header, long id)
    {
        if (header == "") return null;
        var m = IfMatch().Match(header.Trim());
        return m.Success && m.Groups[1].Value == id.ToString() && int.TryParse(m.Groups[2].Value, out var v) ? v : -1;
    }

    /// <summary>Trims the extra row fetched to detect a next page and builds the cursor from the last item.</summary>
    static Page<T> ToPage<T>(List<T> rows, int limit, Func<T, string> cursorOf)
    {
        if (rows.Count <= limit) return new Page<T>(rows, null, limit);
        var items = rows.GetRange(0, limit);
        return new Page<T>(items, cursorOf(items[^1]), limit);
    }

    static IResult Created<T>(HttpContext ctx, string location, T value)
    {
        ctx.Response.Headers.Location = location;
        return Results.Json(value, statusCode: StatusCodes.Status201Created);
    }

    static async Task<IResult> ListBooks(HttpRequest req, BookRepository books, CancellationToken ct)
    {
        var limit = ParseLimit(req);
        var rawAfter = req.Query["after"].ToString();
        var after = rawAfter == "" ? null : Cursor.Decode<BookCursor>(rawAfter, c => c.Id > 0 && c.Title is not null);
        var rows = await books.Search(req.Query["q"].ToString().Trim(), after, limit + 1, ct);
        return Results.Json(ToPage(rows, limit, b => Cursor.Encode(new BookCursor(b.Title, b.Id))));
    }

    static async Task<IResult> CreateBook(HttpContext ctx, BookRepository books, CancellationToken ct)
    {
        var body = await Body.Read(ctx.Request, "title", "author", "isbn", "year");
        var title = body.Str("title", true, 1, 200);
        var author = body.Str("author", true, 1, 200);
        var isbn = body.Str("isbn", true, 13, 13, Body.Isbn13(), "must be an ISBN-13 starting with 978 or 979");
        var year = body.Integer("year", false, true, 1450, Body.ThisYear, "cannot be in the future");
        body.ThrowIfInvalid();

        var book = await books.Create(new NewBook(isbn!, title!, author!, (int?)year), ct);
        ctx.Response.Headers.ETag = ETag(book);
        return Created(ctx, $"/books/{book.Id}", book);
    }

    static async Task<IResult> GetBook(string id, HttpContext ctx, BookRepository books, CancellationToken ct)
    {
        var bookId = ParseId(id);
        var book = await books.Find(bookId, ct) ?? throw DomainException.NotFound("Book", bookId);
        ctx.Response.Headers.ETag = ETag(book);
        return ctx.Request.Headers.IfNoneMatch.ToString() == ETag(book)
            ? Results.StatusCode(StatusCodes.Status304NotModified)
            : Results.Json(book);
    }

    static async Task<IResult> UpdateBook(string id, HttpContext ctx, BookRepository books, CancellationToken ct)
    {
        var bookId = ParseId(id);
        var body = await Body.Read(ctx.Request, "title", "author", "year");
        var changes = new BookChanges(
            body.Str("title", false, 1, 200),
            body.Str("author", false, 1, 200),
            (int?)body.Integer("year", false, true, 1450, Body.ThisYear, "cannot be in the future"),
            body.Has("year"));
        if (body.Count == 0) body.Fail("body", "at least one field is required");
        body.ThrowIfInvalid();

        var book = await books.Update(bookId, changes, ExpectedVersion(ctx.Request.Headers.IfMatch.ToString(), bookId), ct);
        ctx.Response.Headers.ETag = ETag(book);
        return Results.Json(book);
    }

    static async Task<IResult> AddCopy(string id, HttpContext ctx, BookRepository books, CancellationToken ct)
    {
        var bookId = ParseId(id);
        var body = await Body.Read(ctx.Request, "barcode");
        var barcode = body.Str("barcode", true, 1, 40);
        body.ThrowIfInvalid();

        var copy = await books.AddCopy(bookId, barcode!, ct);
        return Created(ctx, $"/copies/{copy.Id}", copy);
    }

    static async Task<IResult> CreateMember(HttpContext ctx, MemberRepository members, CancellationToken ct)
    {
        var body = await Body.Read(ctx.Request, "email", "name");
        var email = body.Email("email");
        var name = body.Str("name", true, 1, 200);
        body.ThrowIfInvalid();

        var member = await members.Create(email!, name!, ct);
        return Created(ctx, $"/members/{member.Id}", member);
    }

    static async Task<IResult> MemberLoans(string id, HttpRequest req, MemberRepository members, LoanRepository loans, CancellationToken ct)
    {
        var memberId = ParseId(id);
        var limit = ParseLimit(req);
        var status = req.Query["status"].ToString();
        if (status is not ("" or "open" or "all"))
            throw DomainException.Validation(new Dictionary<string, List<string>> { ["status"] = ["must be open or all"] });
        if (!await members.Exists(memberId, ct)) throw DomainException.NotFound("Member", memberId);

        var rawAfter = req.Query["after"].ToString();
        long? before = rawAfter == "" ? null : Cursor.Decode<LoanCursor>(rawAfter, c => c.Id > 0).Id;
        var rows = await loans.ForMember(memberId, status == "open", before, limit + 1, ct);
        return Results.Json(ToPage(rows, limit, l => Cursor.Encode(new LoanCursor(l.Id))));
    }

    static async Task<IResult> Lend(HttpContext ctx, LoanService lending, CancellationToken ct)
    {
        var body = await Body.Read(ctx.Request, "copyId", "memberId");
        var copyId = body.Integer("copyId", true, false, 1, 1L << 53, "is too large");
        var memberId = body.Integer("memberId", true, false, 1, 1L << 53, "is too large");
        body.ThrowIfInvalid();

        var loan = await lending.Lend(copyId!.Value, memberId!.Value, ct);
        return Created(ctx, $"/loans/{loan.Id}", loan);
    }

    static async Task<IResult> ReturnLoan(string id, LoanService lending, CancellationToken ct) =>
        Results.Json(await lending.Return(ParseId(id), ct));
}

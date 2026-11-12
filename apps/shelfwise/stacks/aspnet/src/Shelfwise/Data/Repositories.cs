using Npgsql;
using NpgsqlTypes;
using Shelfwise.Domain;
using static Shelfwise.Data.Sql;

namespace Shelfwise.Data;

public sealed class BookRepository(NpgsqlDataSource db)
{
    const string Cols = "id, isbn, title, author, year, version";

    static Book Read(NpgsqlDataReader r) =>
        new(r.GetInt64(0), r.GetString(1), r.GetString(2), r.GetString(3), r.IsDBNull(4) ? null : r.GetInt32(4), r.GetInt32(5));

    public async Task<Book> Create(NewBook b, CancellationToken ct)
    {
        try
        {
            return (await One(Command(db, $"INSERT INTO books (isbn, title, author, year) VALUES ($1, $2, $3, $4) RETURNING {Cols}",
                P(b.Isbn), P(b.Title), P(b.Author), P(b.Year, NpgsqlDbType.Integer)), Read, ct))!;
        }
        catch (PostgresException e) when (IsUnique(e, "books_isbn_key"))
        {
            throw DomainException.DuplicateIsbn(b.Isbn);
        }
    }

    public Task<Book?> Find(long id, CancellationToken ct) =>
        One(Command(db, $"SELECT {Cols} FROM books WHERE id = $1", P(id)), Read, ct);

    /// <summary>Applies only the fields present; with expectedVersion set, the update is conditional on it.</summary>
    public async Task<Book> Update(long id, BookChanges c, int? expectedVersion, CancellationToken ct)
    {
        var args = new List<NpgsqlParameter> { P(id), P(expectedVersion, NpgsqlDbType.Integer) };
        var sets = new List<string>();
        void Set(string column, NpgsqlParameter p) { args.Add(p); sets.Add($"{column} = ${args.Count}"); }
        if (c.Title is not null) Set("title", P(c.Title));
        if (c.Author is not null) Set("author", P(c.Author));
        if (c.YearSet) Set("year", P(c.Year, NpgsqlDbType.Integer));
        sets.Add("version = version + 1");

        var book = await One(Command(db,
            $"UPDATE books SET {string.Join(", ", sets)} WHERE id = $1 AND ($2::int IS NULL OR version = $2) RETURNING {Cols}",
            args.ToArray()), Read, ct);
        if (book is not null) return book;
        throw await Find(id, ct) is null ? DomainException.NotFound("Book", id) : DomainException.VersionMismatch(id);
    }

    /// <summary>Search by title or author, sorted by (title, id), keyset-paginated.</summary>
    public Task<List<Book>> Search(string? q, BookCursor? after, int limit, CancellationToken ct)
    {
        string? pattern = string.IsNullOrEmpty(q) ? null
            : "%" + q.Replace("\\", "\\\\").Replace("%", "\\%").Replace("_", "\\_") + "%";
        return Many(Command(db, $"""
            SELECT {Cols} FROM books
            WHERE ($1::text IS NULL OR title ILIKE $1 OR author ILIKE $1)
              AND ($2::text IS NULL OR (title, id) > ($2::text, $3::bigint))
            ORDER BY title, id LIMIT $4
            """,
            P(pattern, NpgsqlDbType.Text), P(after?.Title, NpgsqlDbType.Text), P(after?.Id, NpgsqlDbType.Bigint), P(limit)), Read, ct);
    }

    public async Task<Copy> AddCopy(long bookId, string barcode, CancellationToken ct)
    {
        try
        {
            return (await One(Command(db, "INSERT INTO copies (book_id, barcode) VALUES ($1, $2) RETURNING id, status",
                P(bookId), P(barcode)), r => new Copy(r.GetInt64(0), bookId, barcode, r.GetString(1)), ct))!;
        }
        catch (PostgresException e) when (e.SqlState == PostgresErrorCodes.ForeignKeyViolation)
        {
            throw DomainException.NotFound("Book", bookId);
        }
        catch (PostgresException e) when (IsUnique(e, "copies_barcode_key"))
        {
            throw DomainException.DuplicateBarcode(barcode);
        }
    }
}

public sealed class MemberRepository(NpgsqlDataSource db)
{
    public async Task<Member> Create(string email, string name, CancellationToken ct)
    {
        try
        {
            return (await One(Command(db, "INSERT INTO members (email, name) VALUES ($1, $2) RETURNING id", P(email), P(name)),
                r => new Member(r.GetInt64(0), email, name), ct))!;
        }
        catch (PostgresException e) when (IsUnique(e, "members_email_key"))
        {
            throw DomainException.DuplicateEmail(email);
        }
    }

    public Task<bool> Exists(long id, CancellationToken ct) => Sql.Exists(db, "SELECT 1 FROM members WHERE id = $1", id, ct);
}

public sealed class LoanRepository(NpgsqlDataSource db) : ILoanStore
{
    // One query shape for every loan we return: the loan plus its copy's barcode and book title.
    const string SelectLoan = """
        SELECT l.id, l.copy_id, l.member_id, c.book_id, b.title, c.barcode, l.loaned_at, l.due_at, l.returned_at
        FROM loans l JOIN copies c ON c.id = l.copy_id JOIN books b ON b.id = c.book_id
        """;

    static Loan Read(NpgsqlDataReader r) => new(
        r.GetInt64(0), r.GetInt64(1), r.GetInt64(2), r.GetInt64(3), r.GetString(4), r.GetString(5),
        r.GetFieldValue<DateTimeOffset>(6), r.GetFieldValue<DateTimeOffset>(7),
        r.IsDBNull(8) ? null : r.GetFieldValue<DateTimeOffset>(8));

    public Task<bool> MemberExists(long id, CancellationToken ct) => Exists(db, "SELECT 1 FROM members WHERE id = $1", id, ct);

    public Task<bool> CopyExists(long id, CancellationToken ct) => Exists(db, "SELECT 1 FROM copies WHERE id = $1", id, ct);

    public Task<bool> CopyHasOpenLoan(long copyId, CancellationToken ct) =>
        Exists(db, "SELECT 1 FROM loans WHERE copy_id = $1 AND returned_at IS NULL", copyId, ct);

    public async Task<int> CountOpenLoans(long memberId, CancellationToken ct)
    {
        await using var cmd = Command(db, "SELECT count(*) FROM loans WHERE member_id = $1 AND returned_at IS NULL", P(memberId));
        return Convert.ToInt32(await cmd.ExecuteScalarAsync(ct));
    }

    Task<Loan?> Find(long id, CancellationToken ct) => One(Command(db, SelectLoan + " WHERE l.id = $1", P(id)), Read, ct);

    public async Task<Loan> InsertLoan(long copyId, long memberId, DateTimeOffset loanedAt, DateTimeOffset dueAt, CancellationToken ct)
    {
        long id;
        try
        {
            await using var cmd = Command(db,
                "INSERT INTO loans (copy_id, member_id, loaned_at, due_at) VALUES ($1, $2, $3, $4) RETURNING id",
                P(copyId), P(memberId), P(loanedAt), P(dueAt));
            id = (long)(await cmd.ExecuteScalarAsync(ct))!;
        }
        catch (PostgresException e) when (IsUnique(e, "one_open_loan_per_copy"))
        {
            throw DomainException.CopyOnLoan(copyId);
        }
        return (await Find(id, ct))!;
    }

    public async Task<Loan> ReturnLoan(long loanId, DateTimeOffset returnedAt, CancellationToken ct)
    {
        int changed;
        await using (var cmd = Command(db, "UPDATE loans SET returned_at = $2 WHERE id = $1 AND returned_at IS NULL",
            P(loanId), P(returnedAt)))
        {
            changed = await cmd.ExecuteNonQueryAsync(ct);
        }
        var loan = await Find(loanId, ct) ?? throw DomainException.NotFound("Loan", loanId);
        return changed == 0 ? throw DomainException.AlreadyReturned(loanId) : loan;
    }

    /// <summary>A member's loans, newest first, keyset-paginated by id.</summary>
    public Task<List<Loan>> ForMember(long memberId, bool openOnly, long? beforeId, int limit, CancellationToken ct) =>
        Many(Command(db, SelectLoan +
            " WHERE l.member_id = $1 AND (NOT $2 OR l.returned_at IS NULL) AND ($3::bigint IS NULL OR l.id < $3)" +
            " ORDER BY l.id DESC LIMIT $4",
            P(memberId), P(openOnly), P(beforeId, NpgsqlDbType.Bigint), P(limit)), Read, ct);
}

namespace Shelfwise.Domain;

/// <summary>The port the loan rules need. Data.LoanRepository implements it; tests use a fake.</summary>
public interface ILoanStore
{
    Task<bool> MemberExists(long memberId, CancellationToken ct);
    Task<bool> CopyExists(long copyId, CancellationToken ct);
    Task<bool> CopyHasOpenLoan(long copyId, CancellationToken ct);
    Task<int> CountOpenLoans(long memberId, CancellationToken ct);
    /// <summary>Must throw CopyOnLoan if the database rejects a second open loan for the copy.</summary>
    Task<Loan> InsertLoan(long copyId, long memberId, DateTimeOffset loanedAt, DateTimeOffset dueAt, CancellationToken ct);
    Task<Loan> ReturnLoan(long loanId, DateTimeOffset returnedAt, CancellationToken ct);
}

public sealed record LoanRules(int LoanDays, int MaxLoansPerMember);

// TimeProvider (.NET 8+) is the built-in clock abstraction: TimeProvider.System in production.
public sealed class LoanService(ILoanStore store, LoanRules rules, TimeProvider clock)
{
    public async Task<Loan> Lend(long copyId, long memberId, CancellationToken ct = default)
    {
        if (!await store.CopyExists(copyId, ct)) throw DomainException.NotFound("Copy", copyId);
        if (!await store.MemberExists(memberId, ct)) throw DomainException.NotFound("Member", memberId);

        // Friendly checks first; the partial unique index still settles races (Chapter 23).
        if (await store.CopyHasOpenLoan(copyId, ct)) throw DomainException.CopyOnLoan(copyId);
        if (await store.CountOpenLoans(memberId, ct) >= rules.MaxLoansPerMember)
            throw DomainException.LoanLimit(memberId, rules.MaxLoansPerMember);

        var loanedAt = clock.GetUtcNow();
        return await store.InsertLoan(copyId, memberId, loanedAt, loanedAt.AddDays(rules.LoanDays), ct);
    }

    public Task<Loan> Return(long loanId, CancellationToken ct = default) =>
        store.ReturnLoan(loanId, clock.GetUtcNow(), ct);
}

using Shelfwise.Domain;

namespace Shelfwise.Tests;

/// <summary>The loan rules against an in-memory fake: no database, no web host (Chapter 37).</summary>
public class LoanServiceTests
{
    static readonly DateTimeOffset Now = new(2026, 3, 1, 10, 0, 0, TimeSpan.Zero);

    sealed class FixedClock(DateTimeOffset now) : TimeProvider
    {
        public override DateTimeOffset GetUtcNow() => now;
    }

    sealed class FakeStore : ILoanStore
    {
        public HashSet<long> Copies { get; } = [1, 2];
        public HashSet<long> Members { get; } = [1];
        public List<Loan> Loans { get; } = [];

        public Task<bool> MemberExists(long id, CancellationToken ct) => Task.FromResult(Members.Contains(id));
        public Task<bool> CopyExists(long id, CancellationToken ct) => Task.FromResult(Copies.Contains(id));
        public Task<bool> CopyHasOpenLoan(long copyId, CancellationToken ct) =>
            Task.FromResult(Loans.Any(l => l.CopyId == copyId && l.ReturnedAt is null));
        public Task<int> CountOpenLoans(long memberId, CancellationToken ct) =>
            Task.FromResult(Loans.Count(l => l.MemberId == memberId && l.ReturnedAt is null));

        public Task<Loan> InsertLoan(long copyId, long memberId, DateTimeOffset loanedAt, DateTimeOffset dueAt, CancellationToken ct)
        {
            var loan = new Loan(Loans.Count + 1, copyId, memberId, 1, "Dune", $"BC-{copyId}", loanedAt, dueAt, null);
            Loans.Add(loan);
            return Task.FromResult(loan);
        }

        public Task<Loan> ReturnLoan(long loanId, DateTimeOffset at, CancellationToken ct)
        {
            var i = Loans.FindIndex(l => l.Id == loanId);
            if (i < 0) throw DomainException.NotFound("Loan", loanId);
            if (Loans[i].ReturnedAt is not null) throw DomainException.AlreadyReturned(loanId);
            Loans[i] = Loans[i] with { ReturnedAt = at };
            return Task.FromResult(Loans[i]);
        }
    }

    static (LoanService, FakeStore) Make(int max = 5)
    {
        var store = new FakeStore();
        return (new LoanService(store, new LoanRules(21, max), new FixedClock(Now)), store);
    }

    [Fact]
    public async Task Lend_sets_due_date_from_the_rules()
    {
        var (service, _) = Make();
        var loan = await service.Lend(1, 1);
        Assert.Equal(Now.AddDays(21), loan.DueAt);
    }

    [Theory]
    [InlineData(99, 1, "not-found")]
    [InlineData(1, 99, "not-found")]
    public async Task Lend_rejects_unknown_copy_or_member(long copyId, long memberId, string code)
    {
        var (service, _) = Make();
        var e = await Assert.ThrowsAsync<DomainException>(() => service.Lend(copyId, memberId));
        Assert.Equal(code, e.Code);
    }

    [Fact]
    public async Task Lend_rejects_a_copy_already_on_loan()
    {
        var (service, _) = Make();
        await service.Lend(1, 1);
        var e = await Assert.ThrowsAsync<DomainException>(() => service.Lend(1, 1));
        Assert.Equal("copy-on-loan", e.Code);
    }

    [Fact]
    public async Task Lend_enforces_the_loan_limit()
    {
        var (service, _) = Make(max: 1);
        await service.Lend(1, 1);
        var e = await Assert.ThrowsAsync<DomainException>(() => service.Lend(2, 1));
        Assert.Equal("loan-limit", e.Code);
        Assert.Equal(409, e.Status);
    }

    [Fact]
    public async Task Return_twice_is_already_returned()
    {
        var (service, _) = Make();
        var loan = await service.Lend(1, 1);
        await service.Return(loan.Id);
        var e = await Assert.ThrowsAsync<DomainException>(() => service.Return(loan.Id));
        Assert.Equal("already-returned", e.Code);
    }
}

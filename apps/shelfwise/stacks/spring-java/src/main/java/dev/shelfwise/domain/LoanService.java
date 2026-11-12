package dev.shelfwise.domain;

import dev.shelfwise.domain.Models.Loan;
import java.time.Clock;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/** The loan rules. Plain Java: Spring creates it in ShelfwiseApplication, tests with `new` (Chapter 28). */
public final class LoanService {
    public record Rules(int loanDays, int maxLoansPerMember) {}

    private final LoanStore store;
    private final Rules rules;
    private final Clock clock;

    public LoanService(LoanStore store, Rules rules, Clock clock) {
        this.store = store;
        this.rules = rules;
        this.clock = clock;
    }

    public Loan lend(long copyId, long memberId) {
        if (!store.copyExists(copyId)) throw DomainException.notFound("Copy", copyId);
        if (!store.memberExists(memberId)) throw DomainException.notFound("Member", memberId);

        // Friendly checks first; the partial unique index still settles races (Chapter 23).
        if (store.copyHasOpenLoan(copyId)) throw DomainException.copyOnLoan(copyId);
        if (store.countOpenLoans(memberId) >= rules.maxLoansPerMember())
            throw DomainException.loanLimit(memberId, rules.maxLoansPerMember());

        var loanedAt = clock.instant().truncatedTo(ChronoUnit.MICROS); // Postgres keeps microseconds
        return store.insertLoan(copyId, memberId, loanedAt, loanedAt.plus(Duration.ofDays(rules.loanDays())));
    }

    public Loan returnLoan(long loanId) {
        return store.returnLoan(loanId, clock.instant().truncatedTo(ChronoUnit.MICROS));
    }
}

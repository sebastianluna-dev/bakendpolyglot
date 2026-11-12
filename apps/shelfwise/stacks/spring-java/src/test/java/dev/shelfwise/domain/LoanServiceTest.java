package dev.shelfwise.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.shelfwise.domain.Models.Loan;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** The loan rules against an in-memory fake: no Spring context, no database (Chapter 37). */
class LoanServiceTest {
    static final Instant NOW = Instant.parse("2026-03-01T10:00:00Z");

    static final class FakeStore implements LoanStore {
        final Set<Long> copies = Set.of(1L, 2L);
        final Set<Long> members = Set.of(1L);
        final List<Loan> loans = new ArrayList<>();

        public boolean memberExists(long id) { return members.contains(id); }
        public boolean copyExists(long id) { return copies.contains(id); }
        public boolean copyHasOpenLoan(long copyId) {
            return loans.stream().anyMatch(l -> l.copyId() == copyId && l.returnedAt() == null);
        }
        public int countOpenLoans(long memberId) {
            return (int) loans.stream().filter(l -> l.memberId() == memberId && l.returnedAt() == null).count();
        }
        public Loan insertLoan(long copyId, long memberId, Instant loanedAt, Instant dueAt) {
            var loan = new Loan(loans.size() + 1, copyId, memberId, 1, "Dune", "BC-" + copyId, loanedAt, dueAt, null);
            loans.add(loan);
            return loan;
        }
        public Loan returnLoan(long loanId, Instant at) {
            if (loanId < 1 || loanId > loans.size()) throw DomainException.notFound("Loan", loanId);
            var l = loans.get((int) loanId - 1);
            if (l.returnedAt() != null) throw DomainException.alreadyReturned(loanId);
            var returned = new Loan(l.id(), l.copyId(), l.memberId(), l.bookId(), l.title(), l.barcode(), l.loanedAt(), l.dueAt(), at);
            loans.set((int) loanId - 1, returned);
            return returned;
        }
    }

    static LoanService service(int max) {
        return new LoanService(new FakeStore(), new LoanService.Rules(21, max), Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Test
    void lendSetsTheDueDateFromTheRules() {
        assertEquals(NOW.plus(Duration.ofDays(21)), service(5).lend(1, 1).dueAt());
    }

    @ParameterizedTest
    @CsvSource({"99, 1", "1, 99"})
    void lendRejectsUnknownCopyOrMember(long copyId, long memberId) {
        var e = assertThrows(DomainException.class, () -> service(5).lend(copyId, memberId));
        assertEquals("not-found", e.code());
    }

    @Test
    void lendRejectsACopyAlreadyOnLoan() {
        var s = service(5);
        s.lend(1, 1);
        assertEquals("copy-on-loan", assertThrows(DomainException.class, () -> s.lend(1, 1)).code());
    }

    @Test
    void lendEnforcesTheLoanLimit() {
        var s = service(1);
        s.lend(1, 1);
        var e = assertThrows(DomainException.class, () -> s.lend(2, 1));
        assertEquals("loan-limit", e.code());
        assertEquals(409, e.status());
    }

    @Test
    void returningTwiceIsAlreadyReturned() {
        var s = service(5);
        var loan = s.lend(1, 1);
        s.returnLoan(loan.id());
        assertEquals("already-returned", assertThrows(DomainException.class, () -> s.returnLoan(loan.id())).code());
    }
}

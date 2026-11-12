package dev.shelfwise.domain;

import dev.shelfwise.domain.Models.Loan;
import java.time.Instant;

/** The port the loan rules need. data.LoanRepository implements it; tests use a fake. */
public interface LoanStore {
    boolean memberExists(long memberId);
    boolean copyExists(long copyId);
    boolean copyHasOpenLoan(long copyId);
    int countOpenLoans(long memberId);
    /** Must throw copyOnLoan if the database rejects a second open loan for the copy. */
    Loan insertLoan(long copyId, long memberId, Instant loanedAt, Instant dueAt);
    Loan returnLoan(long loanId, Instant returnedAt);
}

package dev.shelfwise.data;

import dev.shelfwise.domain.DomainException;
import dev.shelfwise.domain.LoanStore;
import dev.shelfwise.domain.Models.Loan;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class LoanRepository implements LoanStore {
    // One query shape for every loan we return: the loan plus its copy's barcode and book title.
    private static final String SELECT_LOAN = """
            SELECT l.id, l.copy_id, l.member_id, c.book_id, b.title, c.barcode, l.loaned_at, l.due_at, l.returned_at
            FROM loans l JOIN copies c ON c.id = l.copy_id JOIN books b ON b.id = c.book_id
            """;
    private final JdbcClient db;

    public LoanRepository(JdbcClient db) {
        this.db = db;
    }

    // pgJDBC maps timestamptz to OffsetDateTime, not Instant, in both directions.
    private static Instant instant(ResultSet rs, int col) throws SQLException {
        var t = rs.getObject(col, OffsetDateTime.class);
        return t == null ? null : t.toInstant();
    }

    private static OffsetDateTime utc(Instant i) {
        return i.atOffset(ZoneOffset.UTC);
    }

    private static Loan read(ResultSet rs, int row) throws SQLException {
        return new Loan(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getLong(4), rs.getString(5), rs.getString(6),
                instant(rs, 7), instant(rs, 8), instant(rs, 9));
    }

    private boolean exists(String sql, long id) {
        return db.sql("SELECT EXISTS (" + sql + ")").param("id", id).query(Boolean.class).single();
    }

    @Override public boolean memberExists(long id) { return exists("SELECT 1 FROM members WHERE id = :id", id); }

    @Override public boolean copyExists(long id) { return exists("SELECT 1 FROM copies WHERE id = :id", id); }

    @Override public boolean copyHasOpenLoan(long copyId) {
        return exists("SELECT 1 FROM loans WHERE copy_id = :id AND returned_at IS NULL", copyId);
    }

    @Override public int countOpenLoans(long memberId) {
        return db.sql("SELECT count(*) FROM loans WHERE member_id = :id AND returned_at IS NULL")
                .param("id", memberId).query(Integer.class).single();
    }

    private Optional<Loan> find(long id) {
        return db.sql(SELECT_LOAN + " WHERE l.id = :id").param("id", id).query(LoanRepository::read).optional();
    }

    @Override public Loan insertLoan(long copyId, long memberId, Instant loanedAt, Instant dueAt) {
        long id;
        try {
            id = db.sql("INSERT INTO loans (copy_id, member_id, loaned_at, due_at) VALUES (:copyId, :memberId, :loanedAt, :dueAt) RETURNING id")
                    .param("copyId", copyId).param("memberId", memberId)
                    .param("loanedAt", utc(loanedAt)).param("dueAt", utc(dueAt))
                    .query(Long.class).single();
        } catch (DataAccessException e) {
            if (Constraints.violates(e, "one_open_loan_per_copy")) throw DomainException.copyOnLoan(copyId);
            throw e;
        }
        return find(id).orElseThrow();
    }

    @Override public Loan returnLoan(long loanId, Instant returnedAt) {
        int changed = db.sql("UPDATE loans SET returned_at = :at WHERE id = :id AND returned_at IS NULL")
                .param("id", loanId).param("at", utc(returnedAt)).update();
        var loan = find(loanId).orElseThrow(() -> DomainException.notFound("Loan", loanId));
        if (changed == 0) throw DomainException.alreadyReturned(loanId);
        return loan;
    }

    /** A member's loans, newest first, keyset-paginated by id. */
    public List<Loan> forMember(long memberId, boolean openOnly, Long beforeId, int limit) {
        return db.sql(SELECT_LOAN
                        + " WHERE l.member_id = :memberId AND (NOT :openOnly OR l.returned_at IS NULL)"
                        + "   AND (:before::bigint IS NULL OR l.id < :before::bigint)"
                        + " ORDER BY l.id DESC LIMIT :limit")
                .param("memberId", memberId).param("openOnly", openOnly).param("before", beforeId).param("limit", limit)
                .query(LoanRepository::read).list();
    }
}

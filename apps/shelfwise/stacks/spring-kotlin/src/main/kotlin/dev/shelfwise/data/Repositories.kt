package dev.shelfwise.data

import dev.shelfwise.domain.Book
import dev.shelfwise.domain.BookChanges
import dev.shelfwise.domain.BookCursor
import dev.shelfwise.domain.Copy
import dev.shelfwise.domain.DomainException
import dev.shelfwise.domain.Loan
import dev.shelfwise.domain.LoanStore
import dev.shelfwise.domain.Member
import dev.shelfwise.domain.NewBook
import java.sql.ResultSet
import java.sql.SQLException
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import org.postgresql.util.PSQLException
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

// Extension functions: small helpers that read like methods on Spring's own types (Chapter 9).
private fun DataAccessException.violates(constraint: String): Boolean {
    val cause = mostSpecificCause as? PSQLException ?: return false
    return cause.sqlState == "23505" && cause.serverErrorMessage?.constraint == constraint
}

private fun DataAccessException.isForeignKey() = (mostSpecificCause as? SQLException)?.sqlState == "23503"

// pgJDBC maps timestamptz to OffsetDateTime, not Instant, in both directions.
private fun ResultSet.instant(col: Int): Instant? = getObject(col, OffsetDateTime::class.java)?.toInstant()

private fun Instant.utc(): OffsetDateTime = atOffset(ZoneOffset.UTC)

private fun JdbcClient.exists(sql: String, id: Long): Boolean =
    sql("SELECT EXISTS ($sql)").param("id", id).query(Boolean::class.javaObjectType).single()

@Repository
class BookRepository(private val db: JdbcClient) {
    private val cols = "id, isbn, title, author, year, version"

    private val read = RowMapper<Book> { rs, _ ->
        Book(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5).takeUnless { rs.wasNull() }, rs.getInt(6))
    }

    fun create(b: NewBook): Book = try {
        db.sql("INSERT INTO books (isbn, title, author, year) VALUES (:isbn, :title, :author, :year::int) RETURNING $cols")
            .param("isbn", b.isbn).param("title", b.title).param("author", b.author).param("year", b.year)
            .query(read).single()
    } catch (e: DataAccessException) {
        throw if (e.violates("books_isbn_key")) DomainException.duplicateIsbn(b.isbn) else e
    }

    fun find(id: Long): Book? =
        db.sql("SELECT $cols FROM books WHERE id = :id").param("id", id).query(read).optional().orElse(null)

    /** Applies only the fields present; with expectedVersion set, the update is conditional on it. */
    fun update(id: Long, c: BookChanges, expectedVersion: Int?): Book {
        val sets = buildList {
            if (c.title != null) add("title = :title")
            if (c.author != null) add("author = :author")
            if (c.yearSet) add("year = :year::int")
            add("version = version + 1")
        }
        return db.sql("UPDATE books SET ${sets.joinToString()} WHERE id = :id AND (:expected::int IS NULL OR version = :expected::int) RETURNING $cols")
            .param("id", id).param("expected", expectedVersion)
            .param("title", c.title).param("author", c.author).param("year", c.year)
            .query(read).optional().orElse(null)
            ?: throw if (find(id) != null) DomainException.versionMismatch(id) else DomainException.notFound("Book", id)
    }

    /** Search by title or author, sorted by (title, id), keyset-paginated. */
    fun search(q: String, after: BookCursor?, limit: Int): List<Book> {
        val pattern = q.takeIf { it.isNotEmpty() }
            ?.let { "%" + it.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%" }
        return db.sql(
            """
            SELECT $cols FROM books
            WHERE (:pattern::text IS NULL OR title ILIKE :pattern::text OR author ILIKE :pattern::text)
              AND (:afterTitle::text IS NULL OR (title, id) > (:afterTitle::text, :afterId::bigint))
            ORDER BY title, id LIMIT :limit
            """.trimIndent(),
        )
            .param("pattern", pattern).param("afterTitle", after?.title).param("afterId", after?.id).param("limit", limit)
            .query(read).list()
    }

    fun addCopy(bookId: Long, barcode: String): Copy = try {
        db.sql("INSERT INTO copies (book_id, barcode) VALUES (:bookId, :barcode) RETURNING id, status")
            .param("bookId", bookId).param("barcode", barcode)
            .query(RowMapper<Copy> { rs, _ -> Copy(rs.getLong(1), bookId, barcode, rs.getString(2)) }).single()
    } catch (e: DataAccessException) {
        throw when {
            e.isForeignKey() -> DomainException.notFound("Book", bookId)
            e.violates("copies_barcode_key") -> DomainException.duplicateBarcode(barcode)
            else -> e
        }
    }
}

@Repository
class MemberRepository(private val db: JdbcClient) {
    fun create(email: String, name: String): Member = try {
        val id = db.sql("INSERT INTO members (email, name) VALUES (:email, :name) RETURNING id")
            .param("email", email).param("name", name).query(Long::class.javaObjectType).single()
        Member(id, email, name)
    } catch (e: DataAccessException) {
        throw if (e.violates("members_email_key")) DomainException.duplicateEmail(email) else e
    }

    fun exists(id: Long) = db.exists("SELECT 1 FROM members WHERE id = :id", id)
}

@Repository
class LoanRepository(private val db: JdbcClient) : LoanStore {
    // One query shape for every loan we return: the loan plus its copy's barcode and book title.
    private val selectLoan = """
        SELECT l.id, l.copy_id, l.member_id, c.book_id, b.title, c.barcode, l.loaned_at, l.due_at, l.returned_at
        FROM loans l JOIN copies c ON c.id = l.copy_id JOIN books b ON b.id = c.book_id
    """.trimIndent()

    private val read = RowMapper<Loan> { rs, _ ->
        Loan(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getLong(4), rs.getString(5), rs.getString(6),
            rs.instant(7)!!, rs.instant(8)!!, rs.instant(9))
    }

    override fun memberExists(memberId: Long) = db.exists("SELECT 1 FROM members WHERE id = :id", memberId)

    override fun copyExists(copyId: Long) = db.exists("SELECT 1 FROM copies WHERE id = :id", copyId)

    override fun copyHasOpenLoan(copyId: Long) =
        db.exists("SELECT 1 FROM loans WHERE copy_id = :id AND returned_at IS NULL", copyId)

    override fun countOpenLoans(memberId: Long): Int =
        db.sql("SELECT count(*) FROM loans WHERE member_id = :id AND returned_at IS NULL")
            .param("id", memberId).query(Int::class.javaObjectType).single()

    private fun find(id: Long): Loan? =
        db.sql("$selectLoan WHERE l.id = :id").param("id", id).query(read).optional().orElse(null)

    override fun insertLoan(copyId: Long, memberId: Long, loanedAt: Instant, dueAt: Instant): Loan {
        val id = try {
            db.sql("INSERT INTO loans (copy_id, member_id, loaned_at, due_at) VALUES (:copyId, :memberId, :loanedAt, :dueAt) RETURNING id")
                .param("copyId", copyId).param("memberId", memberId)
                .param("loanedAt", loanedAt.utc()).param("dueAt", dueAt.utc())
                .query(Long::class.javaObjectType).single()
        } catch (e: DataAccessException) {
            throw if (e.violates("one_open_loan_per_copy")) DomainException.copyOnLoan(copyId) else e
        }
        return find(id)!!
    }

    override fun returnLoan(loanId: Long, returnedAt: Instant): Loan {
        val changed = db.sql("UPDATE loans SET returned_at = :at WHERE id = :id AND returned_at IS NULL")
            .param("id", loanId).param("at", returnedAt.utc()).update()
        val loan = find(loanId) ?: throw DomainException.notFound("Loan", loanId)
        if (changed == 0) throw DomainException.alreadyReturned(loanId)
        return loan
    }

    /** A member's loans, newest first, keyset-paginated by id. */
    fun forMember(memberId: Long, openOnly: Boolean, beforeId: Long?, limit: Int): List<Loan> =
        db.sql(
            "$selectLoan WHERE l.member_id = :memberId AND (NOT :openOnly OR l.returned_at IS NULL)" +
                " AND (:before::bigint IS NULL OR l.id < :before::bigint) ORDER BY l.id DESC LIMIT :limit",
        )
            .param("memberId", memberId).param("openOnly", openOnly).param("before", beforeId).param("limit", limit)
            .query(read).list()
}

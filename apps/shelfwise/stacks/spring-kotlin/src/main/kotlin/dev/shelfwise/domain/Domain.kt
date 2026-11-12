package dev.shelfwise.domain

import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

// Data classes: Kotlin's records. Nullable types (Int?, Instant?) say "may be null" in the type itself.
data class Book(val id: Long, val isbn: String, val title: String, val author: String, val year: Int?, val version: Int)

data class Copy(val id: Long, val bookId: Long, val barcode: String, val status: String)

data class Member(val id: Long, val email: String, val name: String)

data class Loan(
    val id: Long, val copyId: Long, val memberId: Long, val bookId: Long, val title: String, val barcode: String,
    val loanedAt: Instant, val dueAt: Instant, val returnedAt: Instant?,
)

/** The shape of every paginated list (Chapter 12). */
data class Page<T>(val items: List<T>, val nextCursor: String?, val limit: Int)

data class NewBook(val isbn: String, val title: String, val author: String, val year: Int?)

/** Only the fields present in a PATCH; yearSet tells "set to null" apart from "absent". */
data class BookChanges(val title: String?, val author: String?, val year: Int?, val yearSet: Boolean)

data class BookCursor(val title: String, val id: Long)

data class LoanCursor(val id: Long)

/** A domain error; the HTTP layer renders it as Problem Details (Chapter 12). */
class DomainException(
    val code: String,
    val status: Int,
    val title: String,
    detail: String,
    val extensions: Map<String, Any?> = emptyMap(),
) : RuntimeException(detail) {
    companion object {
        fun notFound(what: String, id: Any) = DomainException("not-found", 404, "Resource not found", "$what $id does not exist.")
        fun duplicateIsbn(isbn: String) = DomainException(
            "duplicate-isbn", 409, "ISBN already in the catalog", "A book with ISBN $isbn already exists.", mapOf("isbn" to isbn))
        fun duplicateBarcode(barcode: String) = DomainException(
            "duplicate-barcode", 409, "Barcode already in use", "A copy with barcode $barcode already exists.", mapOf("barcode" to barcode))
        fun duplicateEmail(email: String) =
            DomainException("duplicate-email", 409, "Email already registered", "A member with email $email already exists.")
        fun copyOnLoan(copyId: Long) = DomainException(
            "copy-on-loan", 409, "Copy is already on loan", "Copy $copyId has an open loan.", mapOf("copyId" to copyId))
        fun loanLimit(memberId: Long, max: Int) = DomainException(
            "loan-limit", 409, "Loan limit reached", "Member $memberId already has $max open loans.",
            mapOf("memberId" to memberId, "max" to max))
        fun alreadyReturned(loanId: Long) =
            DomainException("already-returned", 409, "Loan already returned", "Loan $loanId was already returned.")
        fun versionMismatch(bookId: Long) = DomainException(
            "version-mismatch", 412, "Book was changed by someone else", "Book $bookId does not match the If-Match version.")
        fun badCursor() =
            DomainException("bad-cursor", 400, "Invalid cursor", "The cursor is malformed or expired; start from the first page.")
        fun invalidId(raw: String) = DomainException("invalid-id", 400, "Invalid id", "\"$raw\" is not a valid id.")
        fun invalidBody() = DomainException("invalid-body", 400, "Malformed JSON", "The request body is not valid JSON.")
        fun validation(errors: Map<String, List<String>>) = DomainException(
            "validation", 422, "Request is invalid", "One or more fields are invalid.", mapOf("errors" to errors))
        fun validation(field: String, message: String) = validation(mapOf(field to listOf(message)))
        fun internal(requestId: String?) =
            DomainException("internal", 500, "Internal error", "Something went wrong. Quote request id $requestId.")
    }
}

/** The port the loan rules need. data.LoanRepository implements it; tests use a fake. */
interface LoanStore {
    fun memberExists(memberId: Long): Boolean
    fun copyExists(copyId: Long): Boolean
    fun copyHasOpenLoan(copyId: Long): Boolean
    fun countOpenLoans(memberId: Long): Int
    /** Must throw copyOnLoan if the database rejects a second open loan for the copy. */
    fun insertLoan(copyId: Long, memberId: Long, loanedAt: Instant, dueAt: Instant): Loan
    fun returnLoan(loanId: Long, returnedAt: Instant): Loan
}

data class LoanRules(val loanDays: Int, val maxLoansPerMember: Int)

/** The loan rules. No Spring annotations: one @Bean builds it, tests use the constructor (Chapter 28). */
class LoanService(private val store: LoanStore, private val rules: LoanRules, private val clock: Clock) {
    fun lend(copyId: Long, memberId: Long): Loan {
        if (!store.copyExists(copyId)) throw DomainException.notFound("Copy", copyId)
        if (!store.memberExists(memberId)) throw DomainException.notFound("Member", memberId)

        // Friendly checks first; the partial unique index still settles races (Chapter 23).
        if (store.copyHasOpenLoan(copyId)) throw DomainException.copyOnLoan(copyId)
        if (store.countOpenLoans(memberId) >= rules.maxLoansPerMember) {
            throw DomainException.loanLimit(memberId, rules.maxLoansPerMember)
        }

        val loanedAt = now()
        return store.insertLoan(copyId, memberId, loanedAt, loanedAt + Duration.ofDays(rules.loanDays.toLong()))
    }

    fun returnLoan(loanId: Long): Loan = store.returnLoan(loanId, now())

    private fun now(): Instant = clock.instant().truncatedTo(ChronoUnit.MICROS) // Postgres keeps microseconds
}

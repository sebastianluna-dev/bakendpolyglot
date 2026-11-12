package dev.shelfwise.domain

import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** The loan rules against an in-memory fake: no Spring context, no database (Chapter 37). */
class LoanServiceTest {
    private val now = Instant.parse("2026-03-01T10:00:00Z")

    private class FakeStore : LoanStore {
        val loans = mutableListOf<Loan>()
        override fun memberExists(memberId: Long) = memberId == 1L
        override fun copyExists(copyId: Long) = copyId in 1L..2L
        override fun copyHasOpenLoan(copyId: Long) = loans.any { it.copyId == copyId && it.returnedAt == null }
        override fun countOpenLoans(memberId: Long) = loans.count { it.memberId == memberId && it.returnedAt == null }
        override fun insertLoan(copyId: Long, memberId: Long, loanedAt: Instant, dueAt: Instant) =
            Loan(loans.size + 1L, copyId, memberId, 1, "Dune", "BC-$copyId", loanedAt, dueAt, null).also { loans += it }
        override fun returnLoan(loanId: Long, returnedAt: Instant): Loan {
            val i = loans.indexOfFirst { it.id == loanId }
            if (i < 0) throw DomainException.notFound("Loan", loanId)
            if (loans[i].returnedAt != null) throw DomainException.alreadyReturned(loanId)
            return loans[i].copy(returnedAt = returnedAt).also { loans[i] = it }
        }
    }

    private fun service(max: Int = 5) = LoanService(FakeStore(), LoanRules(21, max), Clock.fixed(now, ZoneOffset.UTC))

    private fun codeOf(block: () -> Unit) = assertFailsWith<DomainException> { block() }.code

    @Test
    fun `lend sets the due date from the rules`() {
        assertEquals(now + Duration.ofDays(21), service().lend(1, 1).dueAt)
    }

    @Test
    fun `lend rejects an unknown copy or member`() {
        assertEquals("not-found", codeOf { service().lend(99, 1) })
        assertEquals("not-found", codeOf { service().lend(1, 99) })
    }

    @Test
    fun `lend rejects a copy already on loan`() {
        val s = service()
        s.lend(1, 1)
        assertEquals("copy-on-loan", codeOf { s.lend(1, 1) })
    }

    @Test
    fun `lend enforces the loan limit`() {
        val s = service(max = 1)
        s.lend(1, 1)
        assertEquals("loan-limit", codeOf { s.lend(2, 1) })
    }

    @Test
    fun `returning twice is already returned`() {
        val s = service()
        val loan = s.lend(1, 1)
        s.returnLoan(loan.id)
        assertEquals("already-returned", codeOf { s.returnLoan(loan.id) })
    }
}

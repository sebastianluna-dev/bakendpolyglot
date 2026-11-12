package dev.shelfwise.http

import com.fasterxml.jackson.databind.ObjectMapper
import dev.shelfwise.data.BookRepository
import dev.shelfwise.data.LoanRepository
import dev.shelfwise.data.MemberRepository
import dev.shelfwise.domain.Book
import dev.shelfwise.domain.BookChanges
import dev.shelfwise.domain.BookCursor
import dev.shelfwise.domain.Copy
import dev.shelfwise.domain.DomainException
import dev.shelfwise.domain.Loan
import dev.shelfwise.domain.LoanCursor
import dev.shelfwise.domain.LoanService
import dev.shelfwise.domain.Member
import dev.shelfwise.domain.NewBook
import dev.shelfwise.domain.Page
import java.net.URI
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private const val NOT_FUTURE = "cannot be in the future"

/**
 * The handler layer (Chapter 18). Ids arrive as Strings and bodies as raw text, so a bad id or
 * malformed JSON becomes Shelfwise's own problem instead of Spring's default 400.
 */
@RestController
class BooksController(private val books: BookRepository, private val json: ObjectMapper) {

    @GetMapping("/health")
    fun health() = mapOf("status" to "ok")

    @GetMapping("/books")
    fun list(
        @RequestParam(required = false) q: String?,
        @RequestParam(required = false) after: String?,
        @RequestParam(required = false) limit: String?,
    ): Page<Book> {
        val n = parseLimit(limit)
        val cursor = after?.takeIf { it.isNotEmpty() }?.let { json.decodeCursor<BookCursor>(it) { c -> c.id > 0 } }
        val rows = books.search(q?.trim().orEmpty(), cursor, n + 1)
        return page(rows, n) { json.encodeCursor(BookCursor(it.title, it.id)) }
    }

    @PostMapping("/books")
    fun create(@RequestBody(required = false) raw: String?): ResponseEntity<Book> {
        val body = Body.read(json, raw, "title", "author", "isbn", "year")
        val title = body.str("title", true, 1..200)
        val author = body.str("author", true, 1..200)
        val isbn = body.str("isbn", true, 13..13, Body.ISBN13, "must be an ISBN-13 starting with 978 or 979")
        val year = body.long("year", false, true, 1450..Body.thisYear(), NOT_FUTURE)
        body.throwIfInvalid()

        val book = books.create(NewBook(isbn!!, title!!, author!!, year?.toInt()))
        return ResponseEntity.created(URI.create("/books/${book.id}")).eTag(book.etag).body(book)
    }

    @GetMapping("/books/{id}")
    fun get(
        @PathVariable id: String,
        @RequestHeader("If-None-Match", required = false) ifNoneMatch: String?,
    ): ResponseEntity<Book> {
        val bookId = parseId(id)
        val book = books.find(bookId) ?: throw DomainException.notFound("Book", bookId)
        if (ifNoneMatch == book.etag) return ResponseEntity.status(HttpStatus.NOT_MODIFIED).eTag(book.etag).build()
        return ResponseEntity.ok().eTag(book.etag).body(book)
    }

    @PatchMapping("/books/{id}")
    fun update(
        @PathVariable id: String,
        @RequestHeader("If-Match", required = false) ifMatch: String?,
        @RequestBody(required = false) raw: String?,
    ): ResponseEntity<Book> {
        val bookId = parseId(id)
        val body = Body.read(json, raw, "title", "author", "year")
        val changes = BookChanges(
            title = body.str("title", false, 1..200),
            author = body.str("author", false, 1..200),
            year = body.long("year", false, true, 1450..Body.thisYear(), NOT_FUTURE)?.toInt(),
            yearSet = body.has("year"),
        )
        if (body.size == 0) body.fail("body", "at least one field is required")
        body.throwIfInvalid()

        val book = books.update(bookId, changes, expectedVersion(ifMatch, bookId))
        return ResponseEntity.ok().eTag(book.etag).body(book)
    }

    @PostMapping("/books/{id}/copies")
    fun addCopy(@PathVariable id: String, @RequestBody(required = false) raw: String?): ResponseEntity<Copy> {
        val bookId = parseId(id)
        val body = Body.read(json, raw, "barcode")
        val barcode = body.str("barcode", true, 1..40)
        body.throwIfInvalid()

        val copy = books.addCopy(bookId, barcode!!)
        return ResponseEntity.created(URI.create("/copies/${copy.id}")).body(copy)
    }
}

/** Members and loans. The rules live in LoanService; this class only translates HTTP. */
@RestController
class LendingController(
    private val members: MemberRepository,
    private val loans: LoanRepository,
    private val lending: LoanService,
    private val json: ObjectMapper,
) {
    @PostMapping("/members")
    fun createMember(@RequestBody(required = false) raw: String?): ResponseEntity<Member> {
        val body = Body.read(json, raw, "email", "name")
        val email = body.email("email")
        val name = body.str("name", true, 1..200)
        body.throwIfInvalid()

        val member = members.create(email!!, name!!)
        return ResponseEntity.created(URI.create("/members/${member.id}")).body(member)
    }

    @GetMapping("/members/{id}/loans")
    fun memberLoans(
        @PathVariable id: String,
        @RequestParam(required = false) status: String?,
        @RequestParam(required = false) after: String?,
        @RequestParam(required = false) limit: String?,
    ): Page<Loan> {
        val memberId = parseId(id)
        val n = parseLimit(limit)
        if (!status.isNullOrEmpty() && status != "open" && status != "all") {
            throw DomainException.validation("status", "must be open or all")
        }
        if (!members.exists(memberId)) throw DomainException.notFound("Member", memberId)

        val before = after?.takeIf { it.isNotEmpty() }?.let { json.decodeCursor<LoanCursor>(it) { c -> c.id > 0 }.id }
        val rows = loans.forMember(memberId, status == "open", before, n + 1)
        return page(rows, n) { json.encodeCursor(LoanCursor(it.id)) }
    }

    @PostMapping("/loans")
    fun lend(@RequestBody(required = false) raw: String?): ResponseEntity<Loan> {
        val body = Body.read(json, raw, "copyId", "memberId")
        val copyId = body.long("copyId", true, false, 1L..(1L shl 53), "is too large")
        val memberId = body.long("memberId", true, false, 1L..(1L shl 53), "is too large")
        body.throwIfInvalid()

        val loan = lending.lend(copyId!!, memberId!!)
        return ResponseEntity.created(URI.create("/loans/${loan.id}")).body(loan)
    }

    @PostMapping("/loans/{id}/return")
    fun returnLoan(@PathVariable id: String): Loan = lending.returnLoan(parseId(id))
}

package dev.shelfwise.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shelfwise.data.BookRepository;
import dev.shelfwise.domain.DomainException;
import dev.shelfwise.domain.Models.Book;
import dev.shelfwise.domain.Models.BookChanges;
import dev.shelfwise.domain.Models.BookCursor;
import dev.shelfwise.domain.Models.Copy;
import dev.shelfwise.domain.Models.NewBook;
import dev.shelfwise.domain.Models.Page;
import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The handler layer for books (Chapter 18). Ids arrive as Strings and bodies as raw text, so a bad id
 * or malformed JSON becomes Shelfwise's own problem instead of Spring's default 400.
 */
@RestController
class BooksController {
    private final BookRepository books;
    private final ObjectMapper json;

    BooksController(BookRepository books, ObjectMapper json) {
        this.books = books;
        this.json = json;
    }

    @GetMapping("/health")
    Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @GetMapping("/books")
    Page<Book> list(@RequestParam(required = false) String q,
                    @RequestParam(required = false) String after,
                    @RequestParam(required = false) String limit) {
        int n = Http.parseLimit(limit);
        BookCursor cursor = after == null || after.isEmpty() ? null
                : Cursors.decode(json, after, BookCursor.class, c -> c.id() > 0 && c.title() != null);
        var rows = books.search(q == null ? "" : q.trim(), cursor, n + 1);
        return Http.page(rows, n, b -> Cursors.encode(json, new BookCursor(b.title(), b.id())));
    }

    @PostMapping("/books")
    ResponseEntity<Book> create(@RequestBody(required = false) String raw) {
        var body = Body.read(json, raw, "title", "author", "isbn", "year");
        var title = body.str("title", true, 1, 200);
        var author = body.str("author", true, 1, 200);
        var isbn = body.str("isbn", true, 13, 13, Body.ISBN13, "must be an ISBN-13 starting with 978 or 979");
        var year = body.integer("year", false, true, 1450, Body.thisYear(), "cannot be in the future");
        body.throwIfInvalid();

        var book = books.create(new NewBook(isbn, title, author, year == null ? null : year.intValue()));
        return ResponseEntity.created(URI.create("/books/" + book.id())).eTag(Http.etag(book)).body(book);
    }

    @GetMapping("/books/{id}")
    ResponseEntity<Book> get(@PathVariable String id,
                             @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
        long bookId = Http.parseId(id);
        var book = books.find(bookId).orElseThrow(() -> DomainException.notFound("Book", bookId));
        var etag = Http.etag(book);
        if (etag.equals(ifNoneMatch)) return ResponseEntity.status(HttpStatus.NOT_MODIFIED).eTag(etag).build();
        return ResponseEntity.ok().eTag(etag).body(book);
    }

    @PatchMapping("/books/{id}")
    ResponseEntity<Book> update(@PathVariable String id,
                                @RequestHeader(value = "If-Match", required = false) String ifMatch,
                                @RequestBody(required = false) String raw) {
        long bookId = Http.parseId(id);
        var body = Body.read(json, raw, "title", "author", "year");
        var year = body.integer("year", false, true, 1450, Body.thisYear(), "cannot be in the future");
        var changes = new BookChanges(
                body.str("title", false, 1, 200),
                body.str("author", false, 1, 200),
                year == null ? null : year.intValue(),
                body.has("year"));
        if (body.size() == 0) body.fail("body", "at least one field is required");
        body.throwIfInvalid();

        var book = books.update(bookId, changes, Http.expectedVersion(ifMatch, bookId));
        return ResponseEntity.ok().eTag(Http.etag(book)).body(book);
    }

    @PostMapping("/books/{id}/copies")
    ResponseEntity<Copy> addCopy(@PathVariable String id, @RequestBody(required = false) String raw) {
        long bookId = Http.parseId(id);
        var body = Body.read(json, raw, "barcode");
        var barcode = body.str("barcode", true, 1, 40);
        body.throwIfInvalid();

        var copy = books.addCopy(bookId, barcode);
        return ResponseEntity.created(URI.create("/copies/" + copy.id())).body(copy);
    }
}

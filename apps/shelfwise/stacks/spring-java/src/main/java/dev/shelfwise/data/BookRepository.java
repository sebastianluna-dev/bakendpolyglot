package dev.shelfwise.data;

import dev.shelfwise.domain.DomainException;
import dev.shelfwise.domain.Models.Book;
import dev.shelfwise.domain.Models.BookChanges;
import dev.shelfwise.domain.Models.BookCursor;
import dev.shelfwise.domain.Models.Copy;
import dev.shelfwise.domain.Models.NewBook;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {
    private static final String COLS = "id, isbn, title, author, year, version";
    private final JdbcClient db;

    public BookRepository(JdbcClient db) {
        this.db = db;
    }

    private static Book read(ResultSet rs, int row) throws SQLException {
        return new Book(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
                rs.getObject(5, Integer.class), rs.getInt(6));
    }

    public Book create(NewBook b) {
        try {
            return db.sql("INSERT INTO books (isbn, title, author, year) VALUES (:isbn, :title, :author, :year::int) RETURNING " + COLS)
                    .param("isbn", b.isbn()).param("title", b.title()).param("author", b.author()).param("year", b.year())
                    .query(BookRepository::read).single();
        } catch (DataAccessException e) {
            if (Constraints.violates(e, "books_isbn_key")) throw DomainException.duplicateIsbn(b.isbn());
            throw e;
        }
    }

    public Optional<Book> find(long id) {
        return db.sql("SELECT " + COLS + " FROM books WHERE id = :id").param("id", id).query(BookRepository::read).optional();
    }

    /** Applies only the fields present; with expectedVersion set, the update is conditional on it. */
    public Book update(long id, BookChanges c, Integer expectedVersion) {
        var sets = new ArrayList<String>();
        if (c.title() != null) sets.add("title = :title");
        if (c.author() != null) sets.add("author = :author");
        if (c.yearSet()) sets.add("year = :year::int");
        sets.add("version = version + 1");

        var updated = db.sql("UPDATE books SET " + String.join(", ", sets)
                        + " WHERE id = :id AND (:expected::int IS NULL OR version = :expected::int) RETURNING " + COLS)
                .param("id", id).param("expected", expectedVersion)
                .param("title", c.title()).param("author", c.author()).param("year", c.year())
                .query(BookRepository::read).optional();
        if (updated.isPresent()) return updated.get();
        throw find(id).isPresent() ? DomainException.versionMismatch(id) : DomainException.notFound("Book", id);
    }

    /** Search by title or author, sorted by (title, id), keyset-paginated. */
    public List<Book> search(String q, BookCursor after, int limit) {
        String pattern = q.isEmpty() ? null
                : "%" + q.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%";
        return db.sql("SELECT " + COLS + " FROM books"
                        + " WHERE (:pattern::text IS NULL OR title ILIKE :pattern::text OR author ILIKE :pattern::text)"
                        + "   AND (:afterTitle::text IS NULL OR (title, id) > (:afterTitle::text, :afterId::bigint))"
                        + " ORDER BY title, id LIMIT :limit")
                .param("pattern", pattern)
                .param("afterTitle", after == null ? null : after.title())
                .param("afterId", after == null ? null : after.id())
                .param("limit", limit)
                .query(BookRepository::read).list();
    }

    public Copy addCopy(long bookId, String barcode) {
        try {
            return db.sql("INSERT INTO copies (book_id, barcode) VALUES (:bookId, :barcode) RETURNING id, status")
                    .param("bookId", bookId).param("barcode", barcode)
                    .query((rs, n) -> new Copy(rs.getLong(1), bookId, barcode, rs.getString(2))).single();
        } catch (DataAccessException e) {
            if (Constraints.foreignKey(e)) throw DomainException.notFound("Book", bookId);
            if (Constraints.violates(e, "copies_barcode_key")) throw DomainException.duplicateBarcode(barcode);
            throw e;
        }
    }
}

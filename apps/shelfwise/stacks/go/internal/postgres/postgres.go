// Package postgres implements the domain ports with pgx (Chapter 21). SQL is written by hand,
// one query per method; unique-constraint names map to domain errors.
package postgres

import (
	"context"
	"errors"
	"fmt"
	"strings"
	"time"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgconn"
	"github.com/jackc/pgx/v5/pgxpool"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/domain"
)

type Store struct{ db *pgxpool.Pool }

func New(db *pgxpool.Pool) *Store { return &Store{db} }

func (s *Store) Books() domain.BookStore     { return bookStore{s.db} }
func (s *Store) Members() domain.MemberStore { return memberStore{s.db} }
func (s *Store) Loans() domain.LoanQueries   { return loanStore{s.db} }
func (s *Store) Lending() domain.LoanStore   { return loanStore{s.db} }

func violates(err error, constraint string) bool {
	var pg *pgconn.PgError
	return errors.As(err, &pg) && pg.Code == "23505" && pg.ConstraintName == constraint
}

func foreignKey(err error) bool {
	var pg *pgconn.PgError
	return errors.As(err, &pg) && pg.Code == "23503"
}

func exists(ctx context.Context, db *pgxpool.Pool, sql string, args ...any) (bool, error) {
	var ok bool
	err := db.QueryRow(ctx, "SELECT EXISTS ("+sql+")", args...).Scan(&ok)
	return ok, err
}

// ---- books ----

const bookCols = "id, isbn, title, author, year, version"

type bookStore struct{ db *pgxpool.Pool }

func scanBook(row pgx.Row) (domain.Book, error) {
	var b domain.Book
	err := row.Scan(&b.ID, &b.ISBN, &b.Title, &b.Author, &b.Year, &b.Version)
	return b, err
}

func (s bookStore) Create(ctx context.Context, nb domain.NewBook) (domain.Book, error) {
	b, err := scanBook(s.db.QueryRow(ctx,
		"INSERT INTO books (isbn, title, author, year) VALUES ($1, $2, $3, $4) RETURNING "+bookCols,
		nb.ISBN, nb.Title, nb.Author, nb.Year))
	if violates(err, "books_isbn_key") {
		return b, domain.DuplicateISBN(nb.ISBN)
	}
	return b, err
}

func (s bookStore) Find(ctx context.Context, id int64) (domain.Book, bool, error) {
	b, err := scanBook(s.db.QueryRow(ctx, "SELECT "+bookCols+" FROM books WHERE id = $1", id))
	if errors.Is(err, pgx.ErrNoRows) {
		return b, false, nil
	}
	return b, err == nil, err
}

func (s bookStore) Update(ctx context.Context, id int64, c domain.BookChanges, expected *int) (domain.Book, error) {
	sets, args := []string{}, []any{id, expected}
	add := func(col string, v any) {
		args = append(args, v)
		sets = append(sets, fmt.Sprintf("%s = $%d", col, len(args))) // column names are constants
	}
	if c.Title != nil {
		add("title", *c.Title)
	}
	if c.Author != nil {
		add("author", *c.Author)
	}
	if c.YearSet {
		add("year", c.Year)
	}
	sets = append(sets, "version = version + 1")
	b, err := scanBook(s.db.QueryRow(ctx,
		"UPDATE books SET "+strings.Join(sets, ", ")+
			" WHERE id = $1 AND ($2::int IS NULL OR version = $2) RETURNING "+bookCols, args...))
	if !errors.Is(err, pgx.ErrNoRows) {
		return b, err
	}
	if _, found, ferr := s.Find(ctx, id); ferr != nil {
		return b, ferr
	} else if found {
		return b, domain.VersionMismatch(id)
	}
	return b, domain.NotFound("Book", id)
}

func (s bookStore) Search(ctx context.Context, q string, after *domain.BookCursor, limit int) ([]domain.Book, error) {
	var pattern, afterTitle *string
	var afterID *int64
	if q != "" {
		p := "%" + strings.NewReplacer(`\`, `\\`, `%`, `\%`, `_`, `\_`).Replace(q) + "%"
		pattern = &p
	}
	if after != nil {
		afterTitle, afterID = &after.Title, &after.ID
	}
	rows, err := s.db.Query(ctx, `SELECT `+bookCols+` FROM books
		WHERE ($1::text IS NULL OR title ILIKE $1 OR author ILIKE $1)
		  AND ($2::text IS NULL OR (title, id) > ($2::text, $3::bigint))
		ORDER BY title, id LIMIT $4`, pattern, afterTitle, afterID, limit)
	if err != nil {
		return nil, err
	}
	return pgx.CollectRows(rows, func(r pgx.CollectableRow) (domain.Book, error) { return scanBook(r) })
}

func (s bookStore) AddCopy(ctx context.Context, bookID int64, barcode string) (domain.Copy, error) {
	c := domain.Copy{BookID: bookID, Barcode: barcode}
	err := s.db.QueryRow(ctx, "INSERT INTO copies (book_id, barcode) VALUES ($1, $2) RETURNING id, status",
		bookID, barcode).Scan(&c.ID, &c.Status)
	switch {
	case foreignKey(err):
		return c, domain.NotFound("Book", bookID)
	case violates(err, "copies_barcode_key"):
		return c, domain.DuplicateBarcode(barcode)
	}
	return c, err
}

// ---- members ----

type memberStore struct{ db *pgxpool.Pool }

func (s memberStore) Create(ctx context.Context, email, name string) (domain.Member, error) {
	m := domain.Member{Email: email, Name: name}
	err := s.db.QueryRow(ctx, "INSERT INTO members (email, name) VALUES ($1, $2) RETURNING id", email, name).Scan(&m.ID)
	if violates(err, "members_email_key") {
		return m, domain.DuplicateEmail(email)
	}
	return m, err
}

func (s memberStore) Exists(ctx context.Context, id int64) (bool, error) {
	return exists(ctx, s.db, "SELECT 1 FROM members WHERE id = $1", id)
}

// ---- loans ----

const selectLoan = `SELECT l.id, l.copy_id, l.member_id, c.book_id, b.title, c.barcode, l.loaned_at, l.due_at, l.returned_at
	FROM loans l JOIN copies c ON c.id = l.copy_id JOIN books b ON b.id = c.book_id`

type loanStore struct{ db *pgxpool.Pool }

func scanLoan(row pgx.Row) (domain.Loan, error) {
	var l domain.Loan
	err := row.Scan(&l.ID, &l.CopyID, &l.MemberID, &l.BookID, &l.Title, &l.Barcode, &l.LoanedAt, &l.DueAt, &l.ReturnedAt)
	return l, err
}

func (s loanStore) MemberExists(ctx context.Context, id int64) (bool, error) {
	return memberStore(s).Exists(ctx, id)
}

func (s loanStore) CopyExists(ctx context.Context, id int64) (bool, error) {
	return exists(ctx, s.db, "SELECT 1 FROM copies WHERE id = $1", id)
}

func (s loanStore) CopyHasOpenLoan(ctx context.Context, copyID int64) (bool, error) {
	return exists(ctx, s.db, "SELECT 1 FROM loans WHERE copy_id = $1 AND returned_at IS NULL", copyID)
}

func (s loanStore) CountOpenLoans(ctx context.Context, memberID int64) (int, error) {
	var n int
	err := s.db.QueryRow(ctx, "SELECT count(*) FROM loans WHERE member_id = $1 AND returned_at IS NULL", memberID).Scan(&n)
	return n, err
}

func (s loanStore) find(ctx context.Context, id int64) (domain.Loan, error) {
	return scanLoan(s.db.QueryRow(ctx, selectLoan+" WHERE l.id = $1", id))
}

func (s loanStore) InsertLoan(ctx context.Context, copyID, memberID int64, loanedAt, dueAt time.Time) (domain.Loan, error) {
	var id int64
	err := s.db.QueryRow(ctx, "INSERT INTO loans (copy_id, member_id, loaned_at, due_at) VALUES ($1, $2, $3, $4) RETURNING id",
		copyID, memberID, loanedAt, dueAt).Scan(&id)
	if violates(err, "one_open_loan_per_copy") {
		return domain.Loan{}, domain.CopyOnLoan(copyID)
	} else if err != nil {
		return domain.Loan{}, err
	}
	return s.find(ctx, id)
}

func (s loanStore) ReturnLoan(ctx context.Context, id int64, at time.Time) (domain.Loan, error) {
	tag, err := s.db.Exec(ctx, "UPDATE loans SET returned_at = $2 WHERE id = $1 AND returned_at IS NULL", id, at)
	if err != nil {
		return domain.Loan{}, err
	}
	loan, err := s.find(ctx, id)
	switch {
	case errors.Is(err, pgx.ErrNoRows):
		return loan, domain.NotFound("Loan", id)
	case err != nil:
		return loan, err
	case tag.RowsAffected() == 0:
		return loan, domain.AlreadyReturned(id)
	}
	return loan, nil
}

func (s loanStore) ForMember(ctx context.Context, memberID int64, openOnly bool, before *int64, limit int) ([]domain.Loan, error) {
	rows, err := s.db.Query(ctx, selectLoan+`
		WHERE l.member_id = $1 AND (NOT $2::boolean OR l.returned_at IS NULL) AND ($3::bigint IS NULL OR l.id < $3)
		ORDER BY l.id DESC LIMIT $4`, memberID, openOnly, before, limit)
	if err != nil {
		return nil, err
	}
	return pgx.CollectRows(rows, func(r pgx.CollectableRow) (domain.Loan, error) { return scanLoan(r) })
}

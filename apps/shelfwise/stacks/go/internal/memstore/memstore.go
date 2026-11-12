// Package memstore keeps everything in maps behind one mutex. It implements the same ports as the
// postgres package, which makes it a handy fake for handler tests and for `make dev` without a database.
package memstore

import (
	"context"
	"sort"
	"strings"
	"sync"
	"time"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/domain"
)

type Store struct {
	mu      sync.Mutex
	books   []domain.Book
	copies  []domain.Copy
	members []domain.Member
	loans   []domain.Loan
}

func New() *Store { return &Store{} }

// Books, Members, Loans and Lending expose the store under each port's name.
func (s *Store) Books() domain.BookStore     { return bookStore{s} }
func (s *Store) Members() domain.MemberStore { return memberStore{s} }
func (s *Store) Loans() domain.LoanQueries   { return loanQueries{s} }
func (s *Store) Lending() domain.LoanStore   { return loanStore{s} }

type bookStore struct{ s *Store }

func (b bookStore) Create(_ context.Context, nb domain.NewBook) (domain.Book, error) {
	s := b.s
	s.mu.Lock()
	defer s.mu.Unlock()
	for _, x := range s.books {
		if x.ISBN == nb.ISBN {
			return domain.Book{}, domain.DuplicateISBN(nb.ISBN)
		}
	}
	book := domain.Book{ID: int64(len(s.books) + 1), ISBN: nb.ISBN, Title: nb.Title, Author: nb.Author, Year: nb.Year, Version: 1}
	s.books = append(s.books, book)
	return book, nil
}

func (s *Store) book(id int64) (*domain.Book, bool) {
	if id < 1 || id > int64(len(s.books)) {
		return nil, false
	}
	return &s.books[id-1], true
}

func (b bookStore) Find(_ context.Context, id int64) (domain.Book, bool, error) {
	b.s.mu.Lock()
	defer b.s.mu.Unlock()
	if p, ok := b.s.book(id); ok {
		return *p, true, nil
	}
	return domain.Book{}, false, nil
}

func (b bookStore) Update(_ context.Context, id int64, c domain.BookChanges, expected *int) (domain.Book, error) {
	b.s.mu.Lock()
	defer b.s.mu.Unlock()
	p, ok := b.s.book(id)
	if !ok {
		return domain.Book{}, domain.NotFound("Book", id)
	}
	if expected != nil && *expected != p.Version {
		return domain.Book{}, domain.VersionMismatch(id)
	}
	if c.Title != nil {
		p.Title = *c.Title
	}
	if c.Author != nil {
		p.Author = *c.Author
	}
	if c.YearSet {
		p.Year = c.Year
	}
	p.Version++
	return *p, nil
}

func (b bookStore) Search(_ context.Context, q string, after *domain.BookCursor, limit int) ([]domain.Book, error) {
	b.s.mu.Lock()
	defer b.s.mu.Unlock()
	q = strings.ToLower(q)
	var out []domain.Book
	for _, x := range b.s.books {
		if q != "" && !strings.Contains(strings.ToLower(x.Title), q) && !strings.Contains(strings.ToLower(x.Author), q) {
			continue
		}
		if after != nil && (x.Title < after.Title || (x.Title == after.Title && x.ID <= after.ID)) {
			continue
		}
		out = append(out, x)
	}
	sort.Slice(out, func(i, j int) bool {
		if out[i].Title != out[j].Title {
			return out[i].Title < out[j].Title
		}
		return out[i].ID < out[j].ID
	})
	if len(out) > limit {
		out = out[:limit]
	}
	return out, nil
}

func (b bookStore) AddCopy(_ context.Context, bookID int64, barcode string) (domain.Copy, error) {
	s := b.s
	s.mu.Lock()
	defer s.mu.Unlock()
	if _, ok := s.book(bookID); !ok {
		return domain.Copy{}, domain.NotFound("Book", bookID)
	}
	for _, c := range s.copies {
		if c.Barcode == barcode {
			return domain.Copy{}, domain.DuplicateBarcode(barcode)
		}
	}
	c := domain.Copy{ID: int64(len(s.copies) + 1), BookID: bookID, Barcode: barcode, Status: "available"}
	s.copies = append(s.copies, c)
	return c, nil
}

type memberStore struct{ s *Store }

func (m memberStore) Create(_ context.Context, email, name string) (domain.Member, error) {
	s := m.s
	s.mu.Lock()
	defer s.mu.Unlock()
	for _, x := range s.members {
		if strings.EqualFold(x.Email, email) {
			return domain.Member{}, domain.DuplicateEmail(email)
		}
	}
	x := domain.Member{ID: int64(len(s.members) + 1), Email: email, Name: name}
	s.members = append(s.members, x)
	return x, nil
}

func (m memberStore) Exists(_ context.Context, id int64) (bool, error) {
	m.s.mu.Lock()
	defer m.s.mu.Unlock()
	return id >= 1 && id <= int64(len(m.s.members)), nil
}

type loanQueries struct{ s *Store }

func (l loanQueries) ForMember(_ context.Context, memberID int64, openOnly bool, before *int64, limit int) ([]domain.Loan, error) {
	l.s.mu.Lock()
	defer l.s.mu.Unlock()
	var out []domain.Loan
	for i := len(l.s.loans) - 1; i >= 0 && len(out) < limit; i-- {
		x := l.s.loans[i]
		if x.MemberID != memberID || (openOnly && x.ReturnedAt != nil) || (before != nil && x.ID >= *before) {
			continue
		}
		out = append(out, x)
	}
	return out, nil
}

// loanStore implements domain.LoanStore. Each call locks separately, like separate SQL statements;
// InsertLoan re-checks the open-loan rule, playing the part of the partial unique index.
type loanStore struct{ s *Store }

func (l loanStore) MemberExists(ctx context.Context, id int64) (bool, error) {
	return memberStore(l).Exists(ctx, id)
}

func (l loanStore) CopyExists(_ context.Context, id int64) (bool, error) {
	l.s.mu.Lock()
	defer l.s.mu.Unlock()
	return id >= 1 && id <= int64(len(l.s.copies)), nil
}

func (s *Store) openLoan(copyID int64) bool {
	for _, x := range s.loans {
		if x.CopyID == copyID && x.ReturnedAt == nil {
			return true
		}
	}
	return false
}

func (l loanStore) CopyHasOpenLoan(_ context.Context, copyID int64) (bool, error) {
	l.s.mu.Lock()
	defer l.s.mu.Unlock()
	return l.s.openLoan(copyID), nil
}

func (l loanStore) CountOpenLoans(_ context.Context, memberID int64) (int, error) {
	l.s.mu.Lock()
	defer l.s.mu.Unlock()
	n := 0
	for _, x := range l.s.loans {
		if x.MemberID == memberID && x.ReturnedAt == nil {
			n++
		}
	}
	return n, nil
}

func (l loanStore) InsertLoan(_ context.Context, copyID, memberID int64, loanedAt, dueAt time.Time) (domain.Loan, error) {
	s := l.s
	s.mu.Lock()
	defer s.mu.Unlock()
	if s.openLoan(copyID) {
		return domain.Loan{}, domain.CopyOnLoan(copyID)
	}
	c := s.copies[copyID-1]
	loan := domain.Loan{
		ID: int64(len(s.loans) + 1), CopyID: copyID, MemberID: memberID, BookID: c.BookID,
		Title: s.books[c.BookID-1].Title, Barcode: c.Barcode, LoanedAt: loanedAt, DueAt: dueAt,
	}
	s.loans = append(s.loans, loan)
	return loan, nil
}

func (l loanStore) ReturnLoan(_ context.Context, id int64, at time.Time) (domain.Loan, error) {
	s := l.s
	s.mu.Lock()
	defer s.mu.Unlock()
	if id < 1 || id > int64(len(s.loans)) {
		return domain.Loan{}, domain.NotFound("Loan", id)
	}
	p := &s.loans[id-1]
	if p.ReturnedAt != nil {
		return domain.Loan{}, domain.AlreadyReturned(id)
	}
	p.ReturnedAt = &at
	return *p, nil
}

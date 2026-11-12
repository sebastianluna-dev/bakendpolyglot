package domain

import "context"

// NewBook and BookChanges are the validated inputs the HTTP layer hands to storage.
type NewBook struct {
	ISBN   string
	Title  string
	Author string
	Year   *int
}

// BookChanges lists only the fields present in a PATCH; YearSet distinguishes "set to null" from "absent".
type BookChanges struct {
	Title   *string
	Author  *string
	Year    *int
	YearSet bool
}

type BookCursor struct {
	Title string `json:"title"`
	ID    int64  `json:"id"`
}

// BookStore, MemberStore and LoanQueries are the ports the HTTP layer uses for reads and simple writes.
type BookStore interface {
	Create(ctx context.Context, b NewBook) (Book, error)
	Find(ctx context.Context, id int64) (Book, bool, error)
	// Update applies changes; with expectedVersion set it is conditional (VersionMismatch / NotFound).
	Update(ctx context.Context, id int64, c BookChanges, expectedVersion *int) (Book, error)
	Search(ctx context.Context, q string, after *BookCursor, limit int) ([]Book, error)
	AddCopy(ctx context.Context, bookID int64, barcode string) (Copy, error)
}

type MemberStore interface {
	Create(ctx context.Context, email, name string) (Member, error)
	Exists(ctx context.Context, id int64) (bool, error)
}

type LoanQueries interface {
	ForMember(ctx context.Context, memberID int64, openOnly bool, beforeID *int64, limit int) ([]Loan, error)
}

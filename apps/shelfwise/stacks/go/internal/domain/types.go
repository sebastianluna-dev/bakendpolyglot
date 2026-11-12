package domain

import "time"

type Book struct {
	ID      int64  `json:"id"`
	ISBN    string `json:"isbn"`
	Title   string `json:"title"`
	Author  string `json:"author"`
	Year    *int   `json:"year"` // pointer: null when unknown
	Version int    `json:"version"`
}

type Copy struct {
	ID      int64  `json:"id"`
	BookID  int64  `json:"bookId"`
	Barcode string `json:"barcode"`
	Status  string `json:"status"`
}

type Member struct {
	ID    int64  `json:"id"`
	Email string `json:"email"`
	Name  string `json:"name"`
}

type Loan struct {
	ID         int64      `json:"id"`
	CopyID     int64      `json:"copyId"`
	MemberID   int64      `json:"memberId"`
	BookID     int64      `json:"bookId"`
	Title      string     `json:"title"`
	Barcode    string     `json:"barcode"`
	LoanedAt   time.Time  `json:"loanedAt"`
	DueAt      time.Time  `json:"dueAt"`
	ReturnedAt *time.Time `json:"returnedAt"`
}

// Page is the shape of every paginated list (Chapter 12).
type Page[T any] struct {
	Items      []T     `json:"items"`
	NextCursor *string `json:"nextCursor"`
	Limit      int     `json:"limit"`
}

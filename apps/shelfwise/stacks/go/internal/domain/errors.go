// Package domain holds Shelfwise's types and rules. It imports no HTTP or SQL packages (Chapter 28).
package domain

import "fmt"

// Error is a domain error; the HTTP layer renders it as Problem Details (Chapter 12).
type Error struct {
	Code       string
	Status     int
	Title      string
	Detail     string
	Extensions map[string]any
}

func (e *Error) Error() string { return e.Code + ": " + e.Detail }

func newError(code string, status int, title, detail string, ext map[string]any) *Error {
	return &Error{Code: code, Status: status, Title: title, Detail: detail, Extensions: ext}
}

func NotFound(what string, id any) *Error {
	return newError("not-found", 404, "Resource not found", fmt.Sprintf("%s %v does not exist.", what, id), nil)
}

func DuplicateISBN(isbn string) *Error {
	return newError("duplicate-isbn", 409, "ISBN already in the catalog", fmt.Sprintf("A book with ISBN %s already exists.", isbn), map[string]any{"isbn": isbn})
}

func DuplicateBarcode(barcode string) *Error {
	return newError("duplicate-barcode", 409, "Barcode already in use", fmt.Sprintf("A copy with barcode %s already exists.", barcode), map[string]any{"barcode": barcode})
}

func DuplicateEmail(email string) *Error {
	return newError("duplicate-email", 409, "Email already registered", fmt.Sprintf("A member with email %s already exists.", email), nil)
}

func CopyOnLoan(copyID int64) *Error {
	return newError("copy-on-loan", 409, "Copy is already on loan", fmt.Sprintf("Copy %d has an open loan.", copyID), map[string]any{"copyId": copyID})
}

func LoanLimit(memberID int64, max int) *Error {
	return newError("loan-limit", 409, "Loan limit reached", fmt.Sprintf("Member %d already has %d open loans.", memberID, max), map[string]any{"memberId": memberID, "max": max})
}

func AlreadyReturned(loanID int64) *Error {
	return newError("already-returned", 409, "Loan already returned", fmt.Sprintf("Loan %d was already returned.", loanID), nil)
}

func VersionMismatch(bookID int64) *Error {
	return newError("version-mismatch", 412, "Book was changed by someone else", fmt.Sprintf("Book %d does not match the If-Match version.", bookID), nil)
}

func BadCursor() *Error {
	return newError("bad-cursor", 400, "Invalid cursor", "The cursor is malformed or expired; start from the first page.", nil)
}

func InvalidID(raw string) *Error {
	return newError("invalid-id", 400, "Invalid id", fmt.Sprintf("%q is not a valid id.", raw), nil)
}

func InvalidBody() *Error {
	return newError("invalid-body", 400, "Malformed JSON", "The request body is not valid JSON.", nil)
}

// Validation carries one or more messages per field.
func Validation(errors map[string][]string) *Error {
	return newError("validation", 422, "Request is invalid", "One or more fields are invalid.", map[string]any{"errors": errors})
}

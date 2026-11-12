// Package httpapi is the handler layer: parse, validate, call a store or service, write HTTP (Chapter 18).
// It uses only the standard library: the Go 1.22 ServeMux understands methods and path parameters.
package httpapi

import (
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"log/slog"
	"net/http"
	"regexp"
	"strconv"
	"strings"
	"time"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/domain"
)

type Deps struct {
	Books   domain.BookStore
	Members domain.MemberStore
	Loans   domain.LoanQueries
	Lending *domain.LoanService
}

// NewHandler wires every route and the middleware around them.
func NewHandler(d Deps) http.Handler {
	h := &handlers{d}
	mux := http.NewServeMux()
	mux.HandleFunc("GET /health", func(w http.ResponseWriter, _ *http.Request) { writeJSON(w, 200, map[string]string{"status": "ok"}) })
	mux.HandleFunc("GET /books", h.listBooks)
	mux.HandleFunc("POST /books", h.createBook)
	mux.HandleFunc("GET /books/{id}", h.getBook)
	mux.HandleFunc("PATCH /books/{id}", h.updateBook)
	mux.HandleFunc("POST /books/{id}/copies", h.addCopy)
	mux.HandleFunc("POST /members", h.createMember)
	mux.HandleFunc("GET /members/{id}/loans", h.memberLoans)
	mux.HandleFunc("POST /loans", h.lend)
	mux.HandleFunc("POST /loans/{id}/return", h.returnLoan)
	mux.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) { writeError(w, r, domain.NotFound("Route", r.URL.Path)) })
	return requestID(mux)
}

// requestID is middleware: a function from handler to handler (Chapters 7 and 11).
func requestID(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		id := r.Header.Get("X-Request-Id")
		if id == "" {
			b := make([]byte, 16)
			_, _ = rand.Read(b)
			id = hex.EncodeToString(b)
		}
		w.Header().Set("X-Request-Id", id)
		start := time.Now()
		next.ServeHTTP(w, r)
		slog.Debug("request", "method", r.Method, "path", r.URL.Path, "requestId", id, "took", time.Since(start))
	})
}

type handlers struct{ d Deps }

func parseID(raw string) (int64, error) {
	id, err := strconv.ParseInt(raw, 10, 64)
	if err != nil || id < 1 || strings.TrimLeft(raw, "0123456789") != "" {
		return 0, domain.InvalidID(raw)
	}
	return id, nil
}

func parseLimit(r *http.Request) (int, error) {
	raw := r.URL.Query().Get("limit")
	if raw == "" {
		return 20, nil
	}
	n, err := strconv.Atoi(raw)
	if err != nil || n < 1 || n > 100 {
		return 0, domain.Validation(map[string][]string{"limit": {"must be an integer between 1 and 100"}})
	}
	return n, nil
}

func etagOf(b domain.Book) string { return fmt.Sprintf(`"book-%d-v%d"`, b.ID, b.Version) }

var ifMatchRe = regexp.MustCompile(`^"book-(\d+)-v(\d+)"$`)

// expectedVersion parses If-Match; a header naming another book never matches (-1).
func expectedVersion(header string, id int64) *int {
	if header == "" {
		return nil
	}
	m := ifMatchRe.FindStringSubmatch(strings.TrimSpace(header))
	v := -1
	if m != nil && m[1] == strconv.FormatInt(id, 10) {
		v, _ = strconv.Atoi(m[2])
	}
	return &v
}

// page trims the extra row fetched to detect a next page and builds the cursor from the last item.
func page[T any](rows []T, limit int, cursorOf func(T) any) domain.Page[T] {
	p := domain.Page[T]{Items: rows, Limit: limit}
	if p.Items == nil {
		p.Items = []T{}
	}
	if len(rows) > limit {
		p.Items = rows[:limit]
		c := domain.EncodeCursor(cursorOf(p.Items[limit-1]))
		p.NextCursor = &c
	}
	return p
}

func (h *handlers) listBooks(w http.ResponseWriter, r *http.Request) {
	limit, err := parseLimit(r)
	if err != nil {
		writeError(w, r, err)
		return
	}
	var after *domain.BookCursor
	if raw := r.URL.Query().Get("after"); raw != "" {
		var c domain.BookCursor
		if err := domain.DecodeCursor(raw, &c, func() bool { return c.ID > 0 }); err != nil {
			writeError(w, r, err)
			return
		}
		after = &c
	}
	rows, err := h.d.Books.Search(r.Context(), strings.TrimSpace(r.URL.Query().Get("q")), after, limit+1)
	if err != nil {
		writeError(w, r, err)
		return
	}
	writeJSON(w, 200, page(rows, limit, func(b domain.Book) any { return domain.BookCursor{Title: b.Title, ID: b.ID} }))
}

func (h *handlers) createBook(w http.ResponseWriter, r *http.Request) {
	o, err := readObject(r, "title", "author", "isbn", "year")
	if err != nil {
		writeError(w, r, err)
		return
	}
	title := o.str("title", true, 1, 200, nil, "")
	author := o.str("author", true, 1, 200, nil, "")
	isbn := o.str("isbn", true, 13, 13, isbn13, "must be an ISBN-13 starting with 978 or 979")
	year := o.integer("year", false, true, 1450, thisYear(), "cannot be in the future")
	if err := o.err(); err != nil {
		writeError(w, r, err)
		return
	}
	nb := domain.NewBook{ISBN: *isbn, Title: *title, Author: *author}
	if year != nil {
		y := int(*year)
		nb.Year = &y
	}
	book, err := h.d.Books.Create(r.Context(), nb)
	if err != nil {
		writeError(w, r, err)
		return
	}
	w.Header().Set("Location", fmt.Sprintf("/books/%d", book.ID))
	w.Header().Set("ETag", etagOf(book))
	writeJSON(w, http.StatusCreated, book)
}

func (h *handlers) getBook(w http.ResponseWriter, r *http.Request) {
	id, err := parseID(r.PathValue("id"))
	if err != nil {
		writeError(w, r, err)
		return
	}
	book, ok, err := h.d.Books.Find(r.Context(), id)
	if err != nil || !ok {
		writeError(w, r, firstErr(err, domain.NotFound("Book", id)))
		return
	}
	w.Header().Set("ETag", etagOf(book))
	if r.Header.Get("If-None-Match") == etagOf(book) {
		w.WriteHeader(http.StatusNotModified)
		return
	}
	writeJSON(w, 200, book)
}

func (h *handlers) updateBook(w http.ResponseWriter, r *http.Request) {
	id, err := parseID(r.PathValue("id"))
	if err != nil {
		writeError(w, r, err)
		return
	}
	o, err := readObject(r, "title", "author", "year")
	if err != nil {
		writeError(w, r, err)
		return
	}
	c := domain.BookChanges{
		Title:   o.str("title", false, 1, 200, nil, ""),
		Author:  o.str("author", false, 1, 200, nil, ""),
		YearSet: o.has("year"),
	}
	if y := o.integer("year", false, true, 1450, thisYear(), "cannot be in the future"); y != nil {
		v := int(*y)
		c.Year = &v
	}
	if len(o.fields) == 0 {
		o.fail("body", "at least one field is required")
	}
	if err := o.err(); err != nil {
		writeError(w, r, err)
		return
	}
	book, err := h.d.Books.Update(r.Context(), id, c, expectedVersion(r.Header.Get("If-Match"), id))
	if err != nil {
		writeError(w, r, err)
		return
	}
	w.Header().Set("ETag", etagOf(book))
	writeJSON(w, 200, book)
}

func (h *handlers) addCopy(w http.ResponseWriter, r *http.Request) {
	bookID, err := parseID(r.PathValue("id"))
	if err != nil {
		writeError(w, r, err)
		return
	}
	o, err := readObject(r, "barcode")
	if err != nil {
		writeError(w, r, err)
		return
	}
	barcode := o.str("barcode", true, 1, 40, nil, "")
	if err := o.err(); err != nil {
		writeError(w, r, err)
		return
	}
	cp, err := h.d.Books.AddCopy(r.Context(), bookID, *barcode)
	if err != nil {
		writeError(w, r, err)
		return
	}
	w.Header().Set("Location", fmt.Sprintf("/copies/%d", cp.ID))
	writeJSON(w, http.StatusCreated, cp)
}

func (h *handlers) createMember(w http.ResponseWriter, r *http.Request) {
	o, err := readObject(r, "email", "name")
	if err != nil {
		writeError(w, r, err)
		return
	}
	email := o.email("email")
	name := o.str("name", true, 1, 200, nil, "")
	if err := o.err(); err != nil {
		writeError(w, r, err)
		return
	}
	m, err := h.d.Members.Create(r.Context(), *email, *name)
	if err != nil {
		writeError(w, r, err)
		return
	}
	w.Header().Set("Location", fmt.Sprintf("/members/%d", m.ID))
	writeJSON(w, http.StatusCreated, m)
}

func (h *handlers) memberLoans(w http.ResponseWriter, r *http.Request) {
	memberID, err := parseID(r.PathValue("id"))
	if err != nil {
		writeError(w, r, err)
		return
	}
	limit, err := parseLimit(r)
	if err != nil {
		writeError(w, r, err)
		return
	}
	status := r.URL.Query().Get("status")
	if status != "" && status != "open" && status != "all" {
		writeError(w, r, domain.Validation(map[string][]string{"status": {"must be open or all"}}))
		return
	}
	if ok, err := h.d.Members.Exists(r.Context(), memberID); err != nil || !ok {
		writeError(w, r, firstErr(err, domain.NotFound("Member", memberID)))
		return
	}
	var before *int64
	if raw := r.URL.Query().Get("after"); raw != "" {
		var c struct {
			ID int64 `json:"id"`
		}
		if err := domain.DecodeCursor(raw, &c, func() bool { return c.ID > 0 }); err != nil {
			writeError(w, r, err)
			return
		}
		before = &c.ID
	}
	rows, err := h.d.Loans.ForMember(r.Context(), memberID, status == "open", before, limit+1)
	if err != nil {
		writeError(w, r, err)
		return
	}
	writeJSON(w, 200, page(rows, limit, func(l domain.Loan) any { return map[string]int64{"id": l.ID} }))
}

func (h *handlers) lend(w http.ResponseWriter, r *http.Request) {
	o, err := readObject(r, "copyId", "memberId")
	if err != nil {
		writeError(w, r, err)
		return
	}
	copyID := o.integer("copyId", true, false, 1, 1<<53, "is too large")
	memberID := o.integer("memberId", true, false, 1, 1<<53, "is too large")
	if err := o.err(); err != nil {
		writeError(w, r, err)
		return
	}
	loan, err := h.d.Lending.Lend(r.Context(), *copyID, *memberID)
	if err != nil {
		writeError(w, r, err)
		return
	}
	w.Header().Set("Location", fmt.Sprintf("/loans/%d", loan.ID))
	writeJSON(w, http.StatusCreated, loan)
}

func (h *handlers) returnLoan(w http.ResponseWriter, r *http.Request) {
	id, err := parseID(r.PathValue("id"))
	if err != nil {
		writeError(w, r, err)
		return
	}
	loan, err := h.d.Lending.Return(r.Context(), id)
	if err != nil {
		writeError(w, r, err)
		return
	}
	writeJSON(w, 200, loan)
}

func firstErr(errs ...error) error {
	for _, e := range errs {
		if e != nil {
			return e
		}
	}
	return nil
}

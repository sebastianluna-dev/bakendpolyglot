package httpapi_test

import (
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/domain"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/httpapi"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/memstore"
)

// Handler tests run the real routes against the in-memory store: no network, no database.
func newServer(t *testing.T) *httptest.Server {
	s := memstore.New()
	srv := httptest.NewServer(httpapi.NewHandler(httpapi.Deps{
		Books: s.Books(), Members: s.Members(), Loans: s.Loans(),
		Lending: domain.NewLoanService(s.Lending(), domain.LoanRules{LoanDays: 21, MaxLoansPerMember: 5}, nil),
	}))
	t.Cleanup(srv.Close)
	return srv
}

func post(t *testing.T, url, body string) (*http.Response, map[string]any) {
	t.Helper()
	res, err := http.Post(url, "application/json", strings.NewReader(body))
	if err != nil {
		t.Fatal(err)
	}
	defer res.Body.Close()
	var out map[string]any
	_ = json.NewDecoder(res.Body).Decode(&out)
	return res, out
}

func TestCreateBookValidation(t *testing.T) {
	srv := newServer(t)
	cases := []struct {
		name, body string
		status     int
		code       string
	}{
		{"valid", `{"title":"Dune","author":"Frank Herbert","isbn":"9780441013593"}`, 201, ""},
		{"duplicate", `{"title":"Dune","author":"Frank Herbert","isbn":"9780441013593"}`, 409, "duplicate-isbn"},
		{"bad isbn", `{"title":"Dune","author":"F","isbn":"123"}`, 422, "validation"},
		{"unknown field", `{"title":"Dune","author":"F","isbn":"9780441013594","x":1}`, 422, "validation"},
		{"malformed", `{"title":`, 400, "invalid-body"},
	}
	for _, tc := range cases {
		t.Run(tc.name, func(t *testing.T) {
			res, body := post(t, srv.URL+"/books", tc.body)
			if res.StatusCode != tc.status {
				t.Fatalf("status = %d, want %d (%v)", res.StatusCode, tc.status, body)
			}
			if tc.code != "" && !strings.HasSuffix(body["type"].(string), "/"+tc.code) {
				t.Fatalf("type = %v, want …/%s", body["type"], tc.code)
			}
		})
	}
}

func TestIfMatchMismatch(t *testing.T) {
	srv := newServer(t)
	post(t, srv.URL+"/books", `{"title":"Dune","author":"Frank Herbert","isbn":"9780441013593"}`)
	req, _ := http.NewRequest(http.MethodPatch, srv.URL+"/books/1", strings.NewReader(`{"title":"Dune!"}`))
	req.Header.Set("If-Match", `"book-1-v9"`)
	res, err := http.DefaultClient.Do(req)
	if err != nil {
		t.Fatal(err)
	}
	res.Body.Close()
	if res.StatusCode != http.StatusPreconditionFailed {
		t.Fatalf("status = %d, want 412", res.StatusCode)
	}
}

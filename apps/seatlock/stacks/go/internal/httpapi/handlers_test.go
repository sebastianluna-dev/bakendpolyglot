package httpapi_test

import (
	"context"
	"crypto/hmac"
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"errors"
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"
	"time"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/domain"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/httpapi"
)

// fakeVerifier accepts "alice" and "olga" as tokens.
type fakeVerifier struct{}

func (fakeVerifier) Verify(_ context.Context, token string) (domain.Caller, error) {
	switch token {
	case "alice":
		return domain.Caller{Sub: "alice", Roles: []string{"customer"}}, nil
	case "olga":
		return domain.Caller{Sub: "olga", Roles: []string{"box-office"}}, nil
	}
	return domain.Caller{}, errors.New("bad token")
}

// fakeStore embeds the interface: only the methods a test touches need an implementation.
type fakeStore struct {
	domain.Store
	events []domain.PaymentEvent
}

func (f *fakeStore) ApplyPaymentEvent(_ context.Context, e domain.PaymentEvent) error {
	f.events = append(f.events, e)
	return nil
}

func server(t *testing.T, store domain.Store) *httptest.Server {
	srv := httptest.NewServer(httpapi.NewHandler(httpapi.Deps{
		Store: store, Verifier: fakeVerifier{},
		Config: httpapi.Config{HoldTTL: time.Minute, MaxSeatsPerHold: 6, WebhookSecret: []byte("secret")},
	}))
	t.Cleanup(srv.Close)
	return srv
}

func call(t *testing.T, srv *httptest.Server, method, path, token, body string, headers ...string) (*http.Response, map[string]any) {
	t.Helper()
	req, _ := http.NewRequest(method, srv.URL+path, strings.NewReader(body))
	req.Header.Set("Content-Type", "application/json")
	if token != "" {
		req.Header.Set("Authorization", "Bearer "+token)
	}
	for i := 0; i+1 < len(headers); i += 2 {
		req.Header.Set(headers[i], headers[i+1])
	}
	res, err := http.DefaultClient.Do(req)
	if err != nil {
		t.Fatal(err)
	}
	defer res.Body.Close()
	var out map[string]any
	_ = json.NewDecoder(res.Body).Decode(&out)
	return res, out
}

func code(body map[string]any) string {
	s, _ := body["type"].(string)
	return s[strings.LastIndex(s, "/")+1:]
}

func TestAuthenticationAndRoles(t *testing.T) {
	srv := server(t, &fakeStore{})
	cases := []struct {
		name, method, path, token string
		status                    int
		code                      string
	}{
		{"no token", "POST", "/holds", "", 401, "unauthorized"},
		{"bad token", "POST", "/holds", "mallory", 401, "unauthorized"},
		{"customer creating an event", "POST", "/events", "alice", 403, "forbidden"},
		{"box-office holding seats", "POST", "/holds", "olga", 403, "forbidden"},
	}
	for _, tc := range cases {
		t.Run(tc.name, func(t *testing.T) {
			res, body := call(t, srv, tc.method, tc.path, tc.token, `{}`)
			if res.StatusCode != tc.status || code(body) != tc.code {
				t.Fatalf("got %d %s, want %d %s", res.StatusCode, code(body), tc.status, tc.code)
			}
			if tc.status == 401 && !strings.HasPrefix(res.Header.Get("WWW-Authenticate"), "Bearer") {
				t.Fatal("missing Bearer challenge")
			}
		})
	}
}

func TestValidationListsEveryField(t *testing.T) {
	srv := server(t, &fakeStore{})
	res, body := call(t, srv, "POST", "/events", "olga",
		`{"name":"","venue":"Arena","startsAt":"tomorrow","sections":[{"name":"A","rows":27,"seatsPerRow":1,"priceCents":1,"x":1}]}`)
	if res.StatusCode != 422 {
		t.Fatalf("status %d", res.StatusCode)
	}
	errs := body["errors"].(map[string]any)
	for _, k := range []string{"name", "startsAt", "sections[0].rows", "sections[0].x"} {
		if _, ok := errs[k]; !ok {
			t.Errorf("missing %s in %v", k, errs)
		}
	}
	res, body = call(t, srv, "POST", "/holds", "alice", `{"eventId":"nope","seatIds":[]}`)
	if res.StatusCode != 422 || body["errors"].(map[string]any)["eventId"] == nil {
		t.Fatalf("hold validation: %d %v", res.StatusCode, body)
	}
	if res, body = call(t, srv, "POST", "/events", "olga", `{"name": `); res.StatusCode != 400 || code(body) != "invalid-body" {
		t.Fatalf("malformed: %d %v", res.StatusCode, body)
	}
}

func TestWebhookSignature(t *testing.T) {
	store := &fakeStore{}
	srv := server(t, store)
	raw := `{"id":"evt_1","type":"payment_intent.succeeded","data":{"paymentIntentId":"pi_1"}}`
	if res, body := call(t, srv, "POST", "/webhooks/payments", "", raw, "X-Signature", "00"); res.StatusCode != 401 || code(body) != "invalid-signature" {
		t.Fatalf("bad signature: %d", res.StatusCode)
	}
	mac := hmac.New(sha256.New, []byte("secret"))
	mac.Write([]byte(raw))
	if res, _ := call(t, srv, "POST", "/webhooks/payments", "", raw, "X-Signature", hex.EncodeToString(mac.Sum(nil))); res.StatusCode != 204 {
		t.Fatalf("good signature: %d", res.StatusCode)
	}
	if len(store.events) != 1 || store.events[0].PaymentIntentID != "pi_1" {
		t.Fatalf("events %v", store.events)
	}
}

func TestPathIDsMustBeUUIDs(t *testing.T) {
	srv := server(t, &fakeStore{})
	if res, body := call(t, srv, "GET", "/events/42", "", ""); res.StatusCode != 400 || code(body) != "invalid-id" {
		t.Fatalf("%d %v", res.StatusCode, body)
	}
}

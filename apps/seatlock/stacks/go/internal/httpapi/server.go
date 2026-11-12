// Package httpapi is the handler layer: parse, authenticate, authorize, call the store, write HTTP
// (Chapter 18). It uses only the standard library; Postgres, Redis and JWT live behind interfaces.
package httpapi

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"net/http"
	"strings"
	"time"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/domain"
)

type Config struct {
	HoldTTL         time.Duration
	MaxSeatsPerHold int
	WebhookSecret   []byte
}

type Deps struct {
	Store    domain.Store
	Verifier domain.Verifier
	Cache    domain.Cache
	Payments interface {
		CreateIntent(ctx context.Context, orderID string, amountCents int, currency string) (string, error)
	}
	Now    func() time.Time
	Config Config
}

type api struct{ Deps }

// NewHandler wires every route. Public routes are listed explicitly; everything else needs a token.
func NewHandler(d Deps) http.Handler {
	if d.Now == nil {
		d.Now = time.Now
	}
	if d.Cache == nil {
		d.Cache = NoCache{}
	}
	a := &api{d}
	mux := http.NewServeMux()
	mux.HandleFunc("GET /health", func(w http.ResponseWriter, _ *http.Request) { writeJSON(w, 200, map[string]string{"status": "ok"}) })
	mux.HandleFunc("GET /events/{id}", a.getEvent)
	mux.HandleFunc("GET /events/{id}/seats", a.seatMap)
	mux.HandleFunc("POST /webhooks/payments", a.paymentWebhook)

	mux.Handle("POST /events", a.require("box-office", a.createEvent))
	mux.Handle("POST /holds", a.require("customer", a.createHold))
	mux.Handle("GET /holds/{id}", a.require("", a.getHold))
	mux.Handle("DELETE /holds/{id}", a.require("", a.releaseHold))
	mux.Handle("POST /holds/{id}/checkout", a.require("customer", a.checkout))
	mux.Handle("GET /orders/{id}", a.require("", a.getOrder))
	mux.Handle("POST /admin/holds/release-expired", a.require("box-office", a.runReaper)) // the hold-reaper client has it
	mux.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) { writeError(w, r, domain.NotFound("Route", r.URL.Path)) })
	return requestID(mux)
}

func requestID(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		id := r.Header.Get("X-Request-Id")
		if id == "" {
			b := make([]byte, 16)
			_, _ = rand.Read(b)
			id = hex.EncodeToString(b)
		}
		w.Header().Set("X-Request-Id", id)
		next.ServeHTTP(w, r)
	})
}

type callerKey struct{}

func caller(r *http.Request) domain.Caller { return r.Context().Value(callerKey{}).(domain.Caller) }

// require authenticates the bearer token and, when role is set, checks it (Chapters 19 and 21).
func (a *api) require(role string, next http.HandlerFunc) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		scheme, token, ok := strings.Cut(r.Header.Get("Authorization"), " ")
		if !ok || !strings.EqualFold(scheme, "bearer") || token == "" {
			writeError(w, r, domain.Unauthorized())
			return
		}
		c, err := a.Verifier.Verify(r.Context(), token)
		if err != nil {
			writeError(w, r, domain.Unauthorized())
			return
		}
		if role != "" && !c.Has(role) {
			writeError(w, r, domain.Forbidden())
			return
		}
		next(w, r.WithContext(context.WithValue(r.Context(), callerKey{}, c)))
	})
}

func pathID(r *http.Request) (string, error) {
	raw := r.PathValue("id")
	if !isUUID(raw) {
		return "", domain.InvalidID(raw)
	}
	return strings.ToLower(raw), nil
}

// NoCache is the Cache used without Redis: always load.
type NoCache struct{}

func (NoCache) GetOrLoad(_ context.Context, _ string, _ time.Duration, load func() ([]byte, error)) ([]byte, error) {
	return load()
}

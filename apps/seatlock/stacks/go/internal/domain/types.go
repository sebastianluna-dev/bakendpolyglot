package domain

import (
	"context"
	"time"
)

type Section struct {
	Name        string
	Rows        int
	SeatsPerRow int
	PriceCents  int
}

type NewEvent struct {
	Name     string
	Venue    string
	StartsAt time.Time
	Sections []Section
}

type Event struct {
	ID        string    `json:"id"`
	Name      string    `json:"name"`
	Venue     string    `json:"venue"`
	StartsAt  time.Time `json:"startsAt"`
	SeatCount int       `json:"seatCount"`
}

// SeatLayout is the cacheable part of a seat; Status is filled in fresh on every read.
type SeatLayout struct {
	ID         string `json:"id"`
	Section    string `json:"section"`
	Row        string `json:"row"`
	Number     int    `json:"number"`
	PriceCents int    `json:"priceCents"`
	Status     string `json:"status"`
}

type Hold struct {
	ID        string    `json:"id"`
	EventID   string    `json:"eventId"`
	SeatIDs   []string  `json:"seatIds"`
	Status    string    `json:"status"`
	ExpiresAt time.Time `json:"expiresAt"`
}

// Visible reports an active hold past its expiry as expired, whether or not the reaper has run.
func (h Hold) Visible(now time.Time) Hold {
	if h.Status == "active" && !h.ExpiresAt.After(now) {
		h.Status = "expired"
	}
	return h
}

type Order struct {
	ID              string   `json:"id"`
	HoldID          string   `json:"holdId"`
	EventID         string   `json:"eventId"`
	SeatIDs         []string `json:"seatIds"`
	Status          string   `json:"status"`
	TotalCents      int      `json:"totalCents"`
	Currency        string   `json:"currency"`
	PaymentIntentID *string  `json:"paymentIntentId"`
}

type PaymentEvent struct {
	ID              string
	Type            string
	PaymentIntentID string
	OrderID         string // optional; the provider echoes it
}

// StoredResponse is an Idempotency-Key's recorded outcome.
type StoredResponse struct {
	Status int
	Body   []byte
}

// Store is the port to the database. postgres.Store implements it.
type Store interface {
	CreateEvent(ctx context.Context, e NewEvent, createdBy string) (Event, error)
	GetEvent(ctx context.Context, id string) (Event, error)
	SeatLayout(ctx context.Context, eventID string) ([]SeatLayout, error)
	SeatStatuses(ctx context.Context, eventID string) (map[string]string, error)

	// CreateHold claims all seats or none; it returns SeatsUnavailable or NotFound.
	CreateHold(ctx context.Context, userID, eventID string, seatIDs []string, now, expiresAt time.Time) (Hold, error)
	GetHold(ctx context.Context, userID, id string) (Hold, error)
	ReleaseHold(ctx context.Context, userID, id string, now time.Time) error

	// BeginCheckout commits a pending order for the hold (or returns the unfinished one to resume).
	BeginCheckout(ctx context.Context, userID, holdID string, now time.Time) (Order, error)
	AttachPaymentIntent(ctx context.Context, orderID, intentID string) error
	GetOrder(ctx context.Context, userID, id string) (Order, error)

	// ClaimIdempotencyKey inserts the key. It returns (nil, nil) when this request owns it.
	ClaimIdempotencyKey(ctx context.Context, userID, key, requestHash string) (*StoredResponse, error)
	SaveIdempotencyKey(ctx context.Context, userID, key string, status int, body []byte) error
	DropIdempotencyKey(ctx context.Context, userID, key string) error

	ApplyPaymentEvent(ctx context.Context, e PaymentEvent) error
	ReleaseExpiredHolds(ctx context.Context, now time.Time) (int, error)
}

type Caller struct {
	Sub   string
	Roles []string
}

func (c Caller) Has(role string) bool {
	for _, r := range c.Roles {
		if r == role {
			return true
		}
	}
	return false
}

// Verifier validates a bearer token. auth.Verifier implements it with golang-jwt and keyfunc.
type Verifier interface {
	Verify(ctx context.Context, token string) (Caller, error)
}

// Cache stores bytes by key; GetOrLoad runs load at most once per key at a time (singleflight).
type Cache interface {
	GetOrLoad(ctx context.Context, key string, ttl time.Duration, load func() ([]byte, error)) ([]byte, error)
}

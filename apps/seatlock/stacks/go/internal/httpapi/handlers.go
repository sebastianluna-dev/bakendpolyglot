package httpapi

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"net/http"
	"sort"
	"time"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/domain"
)

const maxSeatsPerEvent = 5000

// ---------- events ----------

func (a *api) createEvent(w http.ResponseWriter, r *http.Request) {
	o, err := readObject(r, "name", "venue", "startsAt", "sections")
	if err != nil {
		writeError(w, r, err)
		return
	}
	e := domain.NewEvent{Name: o.str("name", 1, 200), Venue: o.str("venue", 1, 200), StartsAt: o.datetime("startsAt")}
	names, total := map[string]bool{}, 0
	for _, s := range o.objects("sections", 1, 10, "name", "rows", "seatsPerRow", "priceCents") {
		sec := domain.Section{Name: s.str("name", 1, 40), Rows: s.integer("rows", 1, 26),
			SeatsPerRow: s.integer("seatsPerRow", 1, 50), PriceCents: s.integer("priceCents", 0, 1_000_000)}
		if names[sec.Name] {
			o.fail("sections", "section names must be unique")
		}
		names[sec.Name] = true
		total += sec.Rows * sec.SeatsPerRow
		e.Sections = append(e.Sections, sec)
	}
	if total > maxSeatsPerEvent {
		o.fail("sections", fmt.Sprintf("at most %d seats per event", maxSeatsPerEvent))
	}
	if err := o.err(); err != nil {
		writeError(w, r, err)
		return
	}
	event, err := a.Store.CreateEvent(r.Context(), e, caller(r).Sub)
	if err != nil {
		writeError(w, r, err)
		return
	}
	w.Header().Set("Location", "/events/"+event.ID)
	writeJSON(w, http.StatusCreated, event)
}

// getEvent is public and cached: events never change after creation (Chapter 25).
func (a *api) getEvent(w http.ResponseWriter, r *http.Request) {
	id, err := pathID(r)
	if err != nil {
		writeError(w, r, err)
		return
	}
	b, err := a.Cache.GetOrLoad(r.Context(), "event:"+id+":v1", time.Minute, func() ([]byte, error) {
		e, err := a.Store.GetEvent(r.Context(), id)
		if err != nil {
			return nil, err
		}
		return json.Marshal(e)
	})
	if err != nil {
		writeError(w, r, err)
		return
	}
	w.Header().Set("Cache-Control", "public, max-age=60")
	w.Header().Set("Content-Type", "application/json")
	_, _ = w.Write(b)
}

// seatMap caches the layout; availability is read fresh on every request.
func (a *api) seatMap(w http.ResponseWriter, r *http.Request) {
	id, err := pathID(r)
	if err != nil {
		writeError(w, r, err)
		return
	}
	b, err := a.Cache.GetOrLoad(r.Context(), "event:"+id+":seats:v1", 5*time.Minute, func() ([]byte, error) {
		seats, err := a.Store.SeatLayout(r.Context(), id)
		if err != nil {
			return nil, err
		}
		return json.Marshal(seats)
	})
	var layout []domain.SeatLayout
	if err == nil {
		err = json.Unmarshal(b, &layout)
	}
	var status map[string]string
	if err == nil {
		status, err = a.Store.SeatStatuses(r.Context(), id)
	}
	if err != nil {
		writeError(w, r, err)
		return
	}
	for i := range layout {
		layout[i].Status = status[layout[i].ID]
	}
	w.Header().Set("Cache-Control", "no-store")
	writeJSON(w, 200, map[string]any{"eventId": id, "items": layout})
}

// ---------- holds ----------

func (a *api) createHold(w http.ResponseWriter, r *http.Request) {
	o, err := readObject(r, "eventId", "seatIds")
	if err != nil {
		writeError(w, r, err)
		return
	}
	eventID, seatIDs := o.uuid("eventId"), o.uuids("seatIds", a.Config.MaxSeatsPerHold)
	if err := o.err(); err != nil {
		writeError(w, r, err)
		return
	}
	now := a.Now().UTC()
	hold, err := a.Store.CreateHold(r.Context(), caller(r).Sub, eventID, seatIDs, now, now.Add(a.Config.HoldTTL))
	if err != nil {
		writeError(w, r, err)
		return
	}
	sort.Strings(hold.SeatIDs)
	w.Header().Set("Location", "/holds/"+hold.ID)
	writeJSON(w, http.StatusCreated, hold.Visible(now))
}

func (a *api) getHold(w http.ResponseWriter, r *http.Request) {
	id, err := pathID(r)
	if err == nil {
		var hold domain.Hold
		if hold, err = a.Store.GetHold(r.Context(), caller(r).Sub, id); err == nil {
			sort.Strings(hold.SeatIDs)
			writeJSON(w, 200, hold.Visible(a.Now()))
			return
		}
	}
	writeError(w, r, err)
}

func (a *api) releaseHold(w http.ResponseWriter, r *http.Request) {
	id, err := pathID(r)
	if err == nil {
		if err = a.Store.ReleaseHold(r.Context(), caller(r).Sub, id, a.Now()); err == nil {
			w.WriteHeader(http.StatusNoContent)
			return
		}
	}
	writeError(w, r, err)
}

// ---------- checkout with an Idempotency-Key (Chapter 24) ----------

func (a *api) checkout(w http.ResponseWriter, r *http.Request) {
	holdID, err := pathID(r)
	if err != nil {
		writeError(w, r, err)
		return
	}
	key := r.Header.Get("Idempotency-Key")
	if key == "" || len(key) > 200 {
		writeError(w, r, domain.IdempotencyKeyRequired())
		return
	}
	user := caller(r).Sub
	sum := sha256.Sum256([]byte("POST /holds/" + holdID + "/checkout"))
	stored, err := a.Store.ClaimIdempotencyKey(r.Context(), user, key, hex.EncodeToString(sum[:]))
	if err != nil {
		writeError(w, r, err)
		return
	}
	if stored != nil {
		w.Header().Set("Idempotent-Replayed", "true")
		writeStored(w, *stored)
		return
	}

	status, body, err := a.runCheckout(r, user, holdID)
	var de *domain.Error
	switch {
	case err == nil:
	case errors.As(err, &de) && de.Status < 500: // 4xx outcomes are stored like successes
		status, body = de.Status, mustJSON(problemBody(de, r.URL.Path))
	default: // 5xx: forget the key so a retry runs again
		_ = a.Store.DropIdempotencyKey(r.Context(), user, key)
		writeError(w, r, err)
		return
	}
	if err := a.Store.SaveIdempotencyKey(r.Context(), user, key, status, body); err != nil {
		writeError(w, r, err)
		return
	}
	writeStored(w, domain.StoredResponse{Status: status, Body: body})
}

// runCheckout commits a pending order, then calls the provider: never hold locks across a network call.
func (a *api) runCheckout(r *http.Request, user, holdID string) (int, []byte, error) {
	order, err := a.Store.BeginCheckout(r.Context(), user, holdID, a.Now())
	if err != nil {
		return 0, nil, err
	}
	intent, err := a.Payments.CreateIntent(r.Context(), order.ID, order.TotalCents, order.Currency)
	if err != nil {
		return 0, nil, err
	}
	if err := a.Store.AttachPaymentIntent(r.Context(), order.ID, intent); err != nil {
		return 0, nil, err
	}
	if order, err = a.Store.GetOrder(r.Context(), user, order.ID); err != nil {
		return 0, nil, err
	}
	return http.StatusCreated, mustJSON(order), nil
}

func writeStored(w http.ResponseWriter, s domain.StoredResponse) {
	if s.Status >= 400 {
		w.Header().Set("Content-Type", "application/problem+json")
	} else {
		var o struct {
			ID string `json:"id"`
		}
		if json.Unmarshal(s.Body, &o) == nil && o.ID != "" {
			w.Header().Set("Location", "/orders/"+o.ID)
		}
		w.Header().Set("Content-Type", "application/json")
	}
	w.WriteHeader(s.Status)
	_, _ = w.Write(s.Body)
}

func mustJSON(v any) []byte {
	b, _ := json.Marshal(v)
	return b
}

func (a *api) getOrder(w http.ResponseWriter, r *http.Request) {
	id, err := pathID(r)
	if err == nil {
		var order domain.Order
		if order, err = a.Store.GetOrder(r.Context(), caller(r).Sub, id); err == nil {
			writeJSON(w, 200, order)
			return
		}
	}
	writeError(w, r, err)
}

// ---------- payment webhooks: verify the raw bytes, then dedupe and apply (Chapter 24) ----------

func (a *api) paymentWebhook(w http.ResponseWriter, r *http.Request) {
	raw, err := io.ReadAll(io.LimitReader(r.Body, 1<<20))
	if err != nil {
		writeError(w, r, domain.InvalidBody())
		return
	}
	if !SignatureMatches(raw, r.Header.Get("X-Signature"), a.Config.WebhookSecret) {
		writeError(w, r, domain.InvalidSignature())
		return
	}
	var e struct {
		ID   string `json:"id"`
		Type string `json:"type"`
		Data struct {
			PaymentIntentID string `json:"paymentIntentId"`
			OrderID         string `json:"orderId"`
		} `json:"data"`
	}
	if json.Unmarshal(raw, &e) != nil || e.ID == "" || e.Type == "" || e.Data.PaymentIntentID == "" {
		writeError(w, r, domain.InvalidBody())
		return
	}
	if e.Type == "payment_intent.succeeded" || e.Type == "payment_intent.failed" {
		orderID := e.Data.OrderID
		if !isUUID(orderID) {
			orderID = ""
		}
		err := a.Store.ApplyPaymentEvent(r.Context(), domain.PaymentEvent{ID: e.ID, Type: e.Type, PaymentIntentID: e.Data.PaymentIntentID, OrderID: orderID})
		if err != nil {
			writeError(w, r, err)
			return
		}
	}
	w.WriteHeader(http.StatusNoContent)
}

// SignatureMatches compares the hex HMAC-SHA256 of the raw body in constant time.
func SignatureMatches(raw []byte, given string, secret []byte) bool {
	mac := hmac.New(sha256.New, secret)
	mac.Write(raw)
	return hmac.Equal([]byte(given), []byte(hex.EncodeToString(mac.Sum(nil))))
}

// ---------- the reaper, on demand ----------

func (a *api) runReaper(w http.ResponseWriter, r *http.Request) {
	n, err := a.Store.ReleaseExpiredHolds(r.Context(), a.Now())
	if err != nil {
		writeError(w, r, err)
		return
	}
	writeJSON(w, 200, map[string]int{"released": n})
}

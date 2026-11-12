// Package postgres implements domain.Store with pgx (Chapters 15 and 23). SQL is written by hand.
package postgres

import (
	"context"
	"errors"
	"sort"
	"time"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/domain"
)

type Store struct{ db *pgxpool.Pool }

func New(db *pgxpool.Pool) *Store { return &Store{db} }

var _ domain.Store = (*Store)(nil)

// ---------- events ----------

func (s *Store) CreateEvent(ctx context.Context, e domain.NewEvent, createdBy string) (domain.Event, error) {
	var ev domain.Event
	err := pgx.BeginFunc(ctx, s.db, func(tx pgx.Tx) error {
		err := tx.QueryRow(ctx, `INSERT INTO events (name, venue, starts_at, created_by) VALUES ($1, $2, $3, $4)
			RETURNING id::text, name, venue, starts_at`, e.Name, e.Venue, e.StartsAt, createdBy).
			Scan(&ev.ID, &ev.Name, &ev.Venue, &ev.StartsAt)
		if err != nil {
			return err
		}
		var rows [][]any
		for _, sec := range e.Sections {
			for r := 0; r < sec.Rows; r++ {
				for n := 1; n <= sec.SeatsPerRow; n++ {
					rows = append(rows, []any{ev.ID, sec.Name, string(rune('A' + r)), n, sec.PriceCents})
				}
			}
		}
		ev.SeatCount = len(rows)
		// COPY: the fastest way to insert thousands of rows (Chapter 15).
		_, err = tx.CopyFrom(ctx, pgx.Identifier{"seats"}, []string{"event_id", "section", "row_label", "number", "price_cents"}, pgx.CopyFromRows(rows))
		return err
	})
	ev.StartsAt = ev.StartsAt.UTC()
	return ev, err
}

func (s *Store) GetEvent(ctx context.Context, id string) (domain.Event, error) {
	var ev domain.Event
	err := s.db.QueryRow(ctx, `SELECT e.id::text, e.name, e.venue, e.starts_at, (SELECT count(*) FROM seats WHERE event_id = e.id)
		FROM events e WHERE e.id = $1`, id).Scan(&ev.ID, &ev.Name, &ev.Venue, &ev.StartsAt, &ev.SeatCount)
	if errors.Is(err, pgx.ErrNoRows) {
		return ev, domain.NotFound("Event", id)
	}
	ev.StartsAt = ev.StartsAt.UTC()
	return ev, err
}

func (s *Store) eventExists(ctx context.Context, q interface {
	QueryRow(context.Context, string, ...any) pgx.Row
}, id string) (bool, error) {
	var ok bool
	err := q.QueryRow(ctx, `SELECT EXISTS (SELECT 1 FROM events WHERE id = $1)`, id).Scan(&ok)
	return ok, err
}

func (s *Store) SeatLayout(ctx context.Context, eventID string) ([]domain.SeatLayout, error) {
	if ok, err := s.eventExists(ctx, s.db, eventID); err != nil || !ok {
		return nil, firstErr(err, domain.NotFound("Event", eventID))
	}
	rows, err := s.db.Query(ctx, `SELECT id::text, section, row_label, number, price_cents FROM seats
		WHERE event_id = $1 ORDER BY section, row_label, number`, eventID)
	if err != nil {
		return nil, err
	}
	return pgx.CollectRows(rows, func(r pgx.CollectableRow) (domain.SeatLayout, error) {
		var x domain.SeatLayout
		err := r.Scan(&x.ID, &x.Section, &x.Row, &x.Number, &x.PriceCents)
		return x, err
	})
}

func (s *Store) SeatStatuses(ctx context.Context, eventID string) (map[string]string, error) {
	rows, err := s.db.Query(ctx, `SELECT id::text, status FROM seats WHERE event_id = $1`, eventID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	out := map[string]string{}
	for rows.Next() {
		var id, status string
		if err := rows.Scan(&id, &status); err != nil {
			return nil, err
		}
		out[id] = status
	}
	return out, rows.Err()
}

// ---------- holds: one conditional UPDATE whose row count decides (Chapter 23) ----------

var errSeatsTaken = errors.New("seats taken")

func (s *Store) CreateHold(ctx context.Context, userID, eventID string, seatIDs []string, now, expiresAt time.Time) (domain.Hold, error) {
	hold := domain.Hold{EventID: eventID, SeatIDs: seatIDs, Status: "active", ExpiresAt: expiresAt}
	err := pgx.BeginFunc(ctx, s.db, func(tx pgx.Tx) error {
		if ok, err := s.eventExists(ctx, tx, eventID); err != nil || !ok {
			return firstErr(err, domain.NotFound("Event", eventID))
		}
		// Lock the requested rows in id order first, so overlapping holds cannot deadlock.
		if _, err := tx.Exec(ctx, `SELECT id FROM seats WHERE event_id = $1 AND id = ANY($2::uuid[]) ORDER BY id FOR UPDATE`, eventID, seatIDs); err != nil {
			return err
		}
		if err := tx.QueryRow(ctx, `INSERT INTO holds (event_id, user_id, seat_ids, created_at, expires_at)
			VALUES ($1, $2, $3::uuid[], $4, $5) RETURNING id::text`, eventID, userID, seatIDs, now, expiresAt).Scan(&hold.ID); err != nil {
			return err
		}
		tag, err := tx.Exec(ctx, `UPDATE seats SET status = 'held', hold_id = $3
			WHERE event_id = $1 AND id = ANY($2::uuid[]) AND status = 'available'`, eventID, seatIDs, hold.ID)
		if err != nil {
			return err
		}
		if tag.RowsAffected() != int64(len(seatIDs)) {
			return errSeatsTaken // BeginFunc rolls back on error
		}
		return nil
	})
	if !errors.Is(err, errSeatsTaken) {
		return hold, err
	}
	rows, qerr := s.db.Query(ctx, `SELECT id::text FROM seats WHERE event_id = $1 AND id = ANY($2::uuid[]) AND status = 'available'`, eventID, seatIDs)
	if qerr != nil {
		return hold, qerr
	}
	free, qerr := pgx.CollectRows(rows, pgx.RowTo[string])
	if qerr != nil {
		return hold, qerr
	}
	isFree := map[string]bool{}
	for _, id := range free {
		isFree[id] = true
	}
	var taken []string
	for _, id := range seatIDs {
		if !isFree[id] {
			taken = append(taken, id)
		}
	}
	sort.Strings(taken)
	return hold, domain.SeatsUnavailable(taken)
}

// GetHold scopes the query to the owner: someone else's hold does not exist for you (Chapter 21).
func (s *Store) GetHold(ctx context.Context, userID, id string) (domain.Hold, error) {
	var h domain.Hold
	err := s.db.QueryRow(ctx, `SELECT id::text, event_id::text, seat_ids::text[], status, expires_at FROM holds WHERE id = $1 AND user_id = $2`, id, userID).
		Scan(&h.ID, &h.EventID, &h.SeatIDs, &h.Status, &h.ExpiresAt)
	if errors.Is(err, pgx.ErrNoRows) {
		return h, domain.NotFound("Hold", id)
	}
	h.ExpiresAt = h.ExpiresAt.UTC()
	return h, err
}

func (s *Store) ReleaseHold(ctx context.Context, userID, id string, now time.Time) error {
	return pgx.BeginFunc(ctx, s.db, func(tx pgx.Tx) error {
		tag, err := tx.Exec(ctx, `UPDATE holds SET status = 'released'
			WHERE id = $1 AND user_id = $2 AND status = 'active' AND expires_at > $3`, id, userID, now)
		if err != nil {
			return err
		}
		if tag.RowsAffected() == 0 {
			var exists bool
			if err := tx.QueryRow(ctx, `SELECT EXISTS (SELECT 1 FROM holds WHERE id = $1 AND user_id = $2)`, id, userID).Scan(&exists); err != nil {
				return err
			}
			if exists {
				return domain.HoldNotActive(id)
			}
			return domain.NotFound("Hold", id)
		}
		_, err = tx.Exec(ctx, `UPDATE seats SET status = 'available', hold_id = NULL WHERE hold_id = $1 AND status = 'held'`, id)
		return err
	})
}

// ---------- checkout ----------

func (s *Store) BeginCheckout(ctx context.Context, userID, holdID string, now time.Time) (domain.Order, error) {
	order := domain.Order{HoldID: holdID}
	err := pgx.BeginFunc(ctx, s.db, func(tx pgx.Tx) error {
		var status string
		var expires time.Time
		err := tx.QueryRow(ctx, `SELECT status, expires_at FROM holds WHERE id = $1 AND user_id = $2 FOR UPDATE`, holdID, userID).Scan(&status, &expires)
		if errors.Is(err, pgx.ErrNoRows) {
			return domain.NotFound("Hold", holdID)
		} else if err != nil {
			return err
		}
		switch {
		case status == "checked_out":
			var intent *string
			if err := tx.QueryRow(ctx, `SELECT id::text, total_cents, currency, payment_intent_id FROM orders WHERE hold_id = $1`, holdID).
				Scan(&order.ID, &order.TotalCents, &order.Currency, &intent); err != nil {
				return err
			}
			if intent != nil {
				return domain.HoldNotActive(holdID)
			}
			return nil // resume: the provider call failed last time
		case status == "expired" || (status == "active" && !expires.After(now)):
			return domain.HoldExpired(holdID)
		case status != "active":
			return domain.HoldNotActive(holdID)
		}
		if err := tx.QueryRow(ctx, `INSERT INTO orders (hold_id, user_id, total_cents)
			SELECT $1, $2, coalesce(sum(price_cents), 0) FROM seats WHERE hold_id = $1
			RETURNING id::text, total_cents, currency`, holdID, userID).Scan(&order.ID, &order.TotalCents, &order.Currency); err != nil {
			return err
		}
		if _, err := tx.Exec(ctx, `INSERT INTO order_lines (order_id, seat_id, price_cents)
			SELECT $1, id, price_cents FROM seats WHERE hold_id = $2`, order.ID, holdID); err != nil {
			return err
		}
		_, err = tx.Exec(ctx, `UPDATE holds SET status = 'checked_out' WHERE id = $1`, holdID)
		return err
	})
	return order, err
}

func (s *Store) AttachPaymentIntent(ctx context.Context, orderID, intentID string) error {
	_, err := s.db.Exec(ctx, `UPDATE orders SET payment_intent_id = $2, updated_at = now() WHERE id = $1 AND payment_intent_id IS NULL`, orderID, intentID)
	return err
}

func (s *Store) GetOrder(ctx context.Context, userID, id string) (domain.Order, error) {
	var o domain.Order
	err := s.db.QueryRow(ctx, `SELECT o.id::text, o.hold_id::text, h.event_id::text, o.status, o.total_cents, o.currency, o.payment_intent_id,
			array(SELECT seat_id::text FROM order_lines WHERE order_id = o.id ORDER BY seat_id)
		FROM orders o JOIN holds h ON h.id = o.hold_id WHERE o.id = $1 AND o.user_id = $2`, id, userID).
		Scan(&o.ID, &o.HoldID, &o.EventID, &o.Status, &o.TotalCents, &o.Currency, &o.PaymentIntentID, &o.SeatIDs)
	if errors.Is(err, pgx.ErrNoRows) {
		return o, domain.NotFound("Order", id)
	}
	return o, err
}

// ---------- idempotency keys (Chapter 24) ----------

func (s *Store) ClaimIdempotencyKey(ctx context.Context, userID, key, requestHash string) (*domain.StoredResponse, error) {
	tag, err := s.db.Exec(ctx, `INSERT INTO idempotency_keys (key, user_id, request_hash) VALUES ($1, $2, $3) ON CONFLICT DO NOTHING`, key, userID, requestHash)
	if err != nil {
		return nil, err
	}
	if tag.RowsAffected() == 1 {
		return nil, nil // ours: run the operation
	}
	var hash string
	var status *int
	var body []byte
	if err := s.db.QueryRow(ctx, `SELECT request_hash, status, response FROM idempotency_keys WHERE user_id = $1 AND key = $2`, userID, key).
		Scan(&hash, &status, &body); err != nil {
		return nil, err
	}
	switch {
	case hash != requestHash:
		return nil, domain.IdempotencyKeyReuse()
	case status == nil:
		return nil, domain.IdempotencyInProgress()
	}
	return &domain.StoredResponse{Status: *status, Body: body}, nil
}

func (s *Store) SaveIdempotencyKey(ctx context.Context, userID, key string, status int, body []byte) error {
	_, err := s.db.Exec(ctx, `UPDATE idempotency_keys SET status = $3, response = $4 WHERE user_id = $1 AND key = $2`, userID, key, status, body)
	return err
}

func (s *Store) DropIdempotencyKey(ctx context.Context, userID, key string) error {
	_, err := s.db.Exec(ctx, `DELETE FROM idempotency_keys WHERE user_id = $1 AND key = $2`, userID, key)
	return err
}

// ---------- payment events: dedupe by event id, apply only to pending orders ----------

func (s *Store) ApplyPaymentEvent(ctx context.Context, e domain.PaymentEvent) error {
	return pgx.BeginFunc(ctx, s.db, func(tx pgx.Tx) error {
		tag, err := tx.Exec(ctx, `INSERT INTO webhook_events (id, type) VALUES ($1, $2) ON CONFLICT DO NOTHING`, e.ID, e.Type)
		if err != nil || tag.RowsAffected() == 0 {
			return err // duplicate delivery: nothing to do
		}
		var orderID, holdID, status string
		err = tx.QueryRow(ctx, `SELECT id::text, hold_id::text, status FROM orders WHERE payment_intent_id = $1 FOR UPDATE`, e.PaymentIntentID).
			Scan(&orderID, &holdID, &status)
		if errors.Is(err, pgx.ErrNoRows) && e.OrderID != "" {
			// The webhook can beat us to saving the intent id; the provider echoes our order id.
			err = tx.QueryRow(ctx, `SELECT id::text, hold_id::text, status FROM orders WHERE id = $1 AND payment_intent_id IS NULL FOR UPDATE`, e.OrderID).
				Scan(&orderID, &holdID, &status)
		}
		if errors.Is(err, pgx.ErrNoRows) {
			return nil // unknown intent: acknowledge so the provider stops retrying
		} else if err != nil {
			return err
		}
		if status != "pending" {
			return nil // late or out-of-order events never undo a final state
		}
		if e.Type == "payment_intent.succeeded" {
			if _, err := tx.Exec(ctx, `UPDATE orders SET status = 'paid', payment_intent_id = $2, updated_at = now() WHERE id = $1`, orderID, e.PaymentIntentID); err != nil {
				return err
			}
			_, err = tx.Exec(ctx, `UPDATE seats SET status = 'sold' WHERE hold_id = $1`, holdID)
			return err
		}
		if _, err := tx.Exec(ctx, `UPDATE orders SET status = 'failed', payment_intent_id = $2, updated_at = now() WHERE id = $1`, orderID, e.PaymentIntentID); err != nil {
			return err
		}
		if _, err := tx.Exec(ctx, `UPDATE seats SET status = 'available', hold_id = NULL WHERE hold_id = $1`, holdID); err != nil {
			return err
		}
		_, err = tx.Exec(ctx, `UPDATE holds SET status = 'released' WHERE id = $1`, holdID)
		return err
	})
}

// ReleaseExpiredHolds is one statement, idempotent by construction; SKIP LOCKED lets several
// instances run it at once without waiting on each other.
func (s *Store) ReleaseExpiredHolds(ctx context.Context, now time.Time) (int, error) {
	var released int
	err := s.db.QueryRow(ctx, `
		WITH expired AS (
			SELECT id FROM holds WHERE status = 'active' AND expires_at <= $1
			ORDER BY expires_at LIMIT 1000 FOR UPDATE SKIP LOCKED
		), done AS (
			UPDATE holds h SET status = 'expired' FROM expired e WHERE h.id = e.id RETURNING h.id
		), freed AS (
			UPDATE seats s SET status = 'available', hold_id = NULL FROM done WHERE s.hold_id = done.id AND s.status = 'held' RETURNING s.id
		)
		SELECT count(*)::int FROM done`, now).Scan(&released) // data-modifying CTEs always run, read or not
	return released, err
}

func firstErr(errs ...error) error {
	for _, e := range errs {
		if e != nil {
			return e
		}
	}
	return nil
}

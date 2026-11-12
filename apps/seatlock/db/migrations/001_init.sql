-- SeatLock schema (Chapters 23, 24, 27). Shared by every stack.

CREATE TABLE events (
  id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  name        text NOT NULL CHECK (length(name) BETWEEN 1 AND 200),
  venue       text NOT NULL CHECK (length(venue) BETWEEN 1 AND 200),
  starts_at   timestamptz NOT NULL,
  created_by  text NOT NULL,
  created_at  timestamptz NOT NULL DEFAULT now()
);

-- A hold is just state with an expiry: no database lock is held while the user decides.
CREATE TABLE holds (
  id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  event_id    uuid NOT NULL REFERENCES events (id),
  user_id     text NOT NULL,
  seat_ids    uuid[] NOT NULL,           -- what was claimed; seats forget the hold once released
  status      text NOT NULL DEFAULT 'active'
              CHECK (status IN ('active', 'checked_out', 'released', 'expired')),
  created_at  timestamptz NOT NULL DEFAULT now(),
  expires_at  timestamptz NOT NULL,
  CHECK (expires_at > created_at)
);
CREATE INDEX holds_reaper_idx ON holds (expires_at) WHERE status = 'active';

CREATE TABLE seats (
  id           uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  event_id     uuid NOT NULL REFERENCES events (id),
  section      text NOT NULL,
  row_label    text NOT NULL,
  number       int  NOT NULL CHECK (number > 0),
  price_cents  int  NOT NULL CHECK (price_cents >= 0),
  status       text NOT NULL DEFAULT 'available' CHECK (status IN ('available', 'held', 'sold')),
  hold_id      uuid REFERENCES holds (id),
  UNIQUE (event_id, section, row_label, number),
  -- an available seat belongs to no hold; a held or sold seat always does
  CHECK ((status = 'available') = (hold_id IS NULL))
);
CREATE INDEX seats_event_idx ON seats (event_id, section, row_label, number);
CREATE INDEX seats_hold_idx ON seats (hold_id) WHERE hold_id IS NOT NULL;

CREATE TABLE orders (
  id                 uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  hold_id            uuid NOT NULL UNIQUE REFERENCES holds (id),   -- one order per hold, ever
  user_id            text NOT NULL,
  total_cents        int  NOT NULL CHECK (total_cents >= 0),
  currency           text NOT NULL DEFAULT 'USD',
  status             text NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'paid', 'failed')),
  payment_intent_id  text UNIQUE,
  created_at         timestamptz NOT NULL DEFAULT now(),
  updated_at         timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE order_lines (
  order_id     uuid NOT NULL REFERENCES orders (id),
  seat_id      uuid NOT NULL REFERENCES seats (id),
  price_cents  int  NOT NULL,
  PRIMARY KEY (order_id, seat_id)
);
CREATE INDEX order_lines_seat_idx ON order_lines (seat_id);

-- Chapter 24: the primary key is what makes two concurrent retries unable to both run.
CREATE TABLE idempotency_keys (
  key           text        NOT NULL,
  user_id       text        NOT NULL,
  request_hash  text        NOT NULL,
  status        int,                       -- null while the first attempt is running
  response      jsonb,
  created_at    timestamptz NOT NULL DEFAULT now(),
  PRIMARY KEY (user_id, key)
);

-- Webhook deduplication by the provider's event id.
CREATE TABLE webhook_events (
  id           text PRIMARY KEY,
  type         text NOT NULL,
  received_at  timestamptz NOT NULL DEFAULT now()
);

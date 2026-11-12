-- Shelfwise schema (Chapter 14). Shared by every stack.

CREATE TABLE books (
  id         bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  isbn       text NOT NULL UNIQUE CHECK (isbn ~ '^97[89][0-9]{10}$'),
  title      text NOT NULL CHECK (length(title) BETWEEN 1 AND 200),
  author     text NOT NULL CHECK (length(author) BETWEEN 1 AND 200),
  year       int  CHECK (year BETWEEN 1450 AND 2100),
  version    int  NOT NULL DEFAULT 1
);
CREATE INDEX books_title_id_idx ON books (title, id);   -- cursor pagination

CREATE TABLE copies (
  id        bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  book_id   bigint NOT NULL REFERENCES books (id),
  barcode   text   NOT NULL UNIQUE CHECK (length(barcode) BETWEEN 1 AND 40),
  status    text   NOT NULL DEFAULT 'available' CHECK (status IN ('available', 'lost', 'retired')),
  added_at  timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX copies_book_id_idx ON copies (book_id);

CREATE TABLE members (
  id     bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  email  text NOT NULL UNIQUE,
  name   text NOT NULL CHECK (length(name) BETWEEN 1 AND 200),
  since  date NOT NULL DEFAULT current_date
);

CREATE TABLE loans (
  id           bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  copy_id      bigint NOT NULL REFERENCES copies (id),
  member_id    bigint NOT NULL REFERENCES members (id),
  loaned_at    timestamptz NOT NULL DEFAULT now(),
  due_at       timestamptz NOT NULL,
  returned_at  timestamptz,
  CHECK (due_at > loaned_at)
);
CREATE UNIQUE INDEX one_open_loan_per_copy ON loans (copy_id) WHERE returned_at IS NULL;
CREATE INDEX loans_member_open_idx ON loans (member_id) WHERE returned_at IS NULL;
CREATE INDEX loans_member_id_idx ON loans (member_id, id DESC);

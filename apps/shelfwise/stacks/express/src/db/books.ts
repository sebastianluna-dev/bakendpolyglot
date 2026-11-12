import { duplicateIsbn, notFound, versionMismatch } from '../domain/errors.js';
import type { Book } from '../domain/types.js';
import { isUniqueViolation, type Db } from './pool.js';

const COLUMNS = 'id, isbn, title, author, year, version';

export interface NewBook { isbn: string; title: string; author: string; year: number | null }
export type BookChanges = Partial<Pick<Book, 'title' | 'author' | 'year'>>;

export class BookRepository {
  constructor(private readonly db: Db) {}

  async create(b: NewBook): Promise<Book> {
    try {
      const { rows } = await this.db.query<Book>(
        `INSERT INTO books (isbn, title, author, year) VALUES ($1, $2, $3, $4) RETURNING ${COLUMNS}`,
        [b.isbn, b.title, b.author, b.year],
      );
      return rows[0]!;
    } catch (err) {
      if (isUniqueViolation(err, 'books_isbn_key')) throw duplicateIsbn(b.isbn);
      throw err;
    }
  }

  async find(id: number): Promise<Book | null> {
    const { rows } = await this.db.query<Book>(`SELECT ${COLUMNS} FROM books WHERE id = $1`, [id]);
    return rows[0] ?? null;
  }

  /** Applies only the fields present; when expectedVersion is set, the update is conditional on it. */
  async update(id: number, changes: BookChanges, expectedVersion: number | null): Promise<Book> {
    const sets: string[] = [];
    const params: unknown[] = [id, expectedVersion];
    for (const [column, value] of Object.entries(changes)) {
      params.push(value);
      sets.push(`${column} = $${params.length}`); // column names come from the validated DTO keys only
    }
    const { rows } = await this.db.query<Book>(
      `UPDATE books SET ${sets.join(', ')}, version = version + 1
       WHERE id = $1 AND ($2::int IS NULL OR version = $2) RETURNING ${COLUMNS}`,
      params,
    );
    if (rows[0]) return rows[0];
    if (await this.find(id)) throw versionMismatch(id);
    throw notFound('Book', id);
  }

  /** Search by title or author, sorted by (title, id), keyset-paginated. */
  async search(q: string | null, after: { title: string; id: number } | null, limit: number): Promise<Book[]> {
    const pattern = q ? `%${q.replace(/[\\%_]/g, (c) => '\\' + c)}%` : null;
    const { rows } = await this.db.query<Book>(
      `SELECT ${COLUMNS} FROM books
       WHERE ($1::text IS NULL OR title ILIKE $1 OR author ILIKE $1)
         AND ($2::text IS NULL OR (title, id) > ($2::text, $3::bigint))
       ORDER BY title, id
       LIMIT $4`,
      [pattern, after?.title ?? null, after?.id ?? null, limit],
    );
    return rows;
  }
}

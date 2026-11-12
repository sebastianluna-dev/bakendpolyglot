import { Inject, Injectable } from '@nestjs/common';
import type pg from 'pg';
import { POOL, isUniqueViolation } from '../database.module.js';
import { duplicateBarcode, duplicateIsbn, notFound, versionMismatch } from '../domain/errors.js';
import type { Book, Copy } from '../domain/types.js';

const COLUMNS = 'id, isbn, title, author, year, version';
export type BookChanges = Partial<Pick<Book, 'title' | 'author' | 'year'>>;

@Injectable()
export class BooksRepository {
  constructor(@Inject(POOL) private readonly db: pg.Pool) {}

  async create(b: { isbn: string; title: string; author: string; year: number | null }): Promise<Book> {
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
    return (await this.db.query<Book>(`SELECT ${COLUMNS} FROM books WHERE id = $1`, [id])).rows[0] ?? null;
  }

  async update(id: number, changes: BookChanges, expectedVersion: number | null): Promise<Book> {
    const sets: string[] = [];
    const params: unknown[] = [id, expectedVersion];
    for (const [column, value] of Object.entries(changes)) {
      params.push(value);
      sets.push(`${column} = $${params.length}`); // keys come from the whitelisted DTO only
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

  async search(q: string | null, after: { title: string; id: number } | null, limit: number): Promise<Book[]> {
    const pattern = q ? `%${q.replace(/[\\%_]/g, (c) => '\\' + c)}%` : null;
    const { rows } = await this.db.query<Book>(
      `SELECT ${COLUMNS} FROM books
       WHERE ($1::text IS NULL OR title ILIKE $1 OR author ILIKE $1)
         AND ($2::text IS NULL OR (title, id) > ($2::text, $3::bigint))
       ORDER BY title, id LIMIT $4`,
      [pattern, after?.title ?? null, after?.id ?? null, limit],
    );
    return rows;
  }

  async addCopy(bookId: number, barcode: string): Promise<Copy> {
    try {
      const { rows } = await this.db.query<Copy>(
        `INSERT INTO copies (book_id, barcode) VALUES ($1, $2) RETURNING id, book_id AS "bookId", barcode, status`,
        [bookId, barcode],
      );
      return rows[0]!;
    } catch (err) {
      if ((err as { code?: string }).code === '23503') throw notFound('Book', bookId);
      if (isUniqueViolation(err, 'copies_barcode_key')) throw duplicateBarcode(barcode);
      throw err;
    }
  }
}

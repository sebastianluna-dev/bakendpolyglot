import { duplicateBarcode, duplicateEmail, notFound } from '../domain/errors.js';
import type { Copy, Member } from '../domain/types.js';
import { isUniqueViolation, type Db } from './pool.js';

export class CopyRepository {
  constructor(private readonly db: Db) {}

  async create(bookId: number, barcode: string): Promise<Copy> {
    try {
      const { rows } = await this.db.query<Copy>(
        `INSERT INTO copies (book_id, barcode) VALUES ($1, $2)
         RETURNING id, book_id AS "bookId", barcode, status`,
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

export class MemberRepository {
  constructor(private readonly db: Db) {}

  async create(email: string, name: string): Promise<Member> {
    try {
      const { rows } = await this.db.query<Member>(
        'INSERT INTO members (email, name) VALUES ($1, $2) RETURNING id, email, name',
        [email, name],
      );
      return rows[0]!;
    } catch (err) {
      if (isUniqueViolation(err, 'members_email_key')) throw duplicateEmail(email);
      throw err;
    }
  }

  async exists(id: number): Promise<boolean> {
    const { rowCount } = await this.db.query('SELECT 1 FROM members WHERE id = $1', [id]);
    return rowCount === 1;
  }
}

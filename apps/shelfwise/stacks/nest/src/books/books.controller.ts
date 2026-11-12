import { Body, Controller, Get, Headers, HttpCode, Param, Patch, Post, Query, Res } from '@nestjs/common';
import type { Response } from 'express';
import { ParseIdPipe, validationFailed } from '../common/validation.js';
import { decodeCursor, encodeCursor } from '../domain/cursor.js';
import { notFound } from '../domain/errors.js';
import type { Book } from '../domain/types.js';
import { BookListQuery, CreateBookRequest, CreateCopyRequest, UpdateBookRequest } from './books.dto.js';
import { BooksRepository } from './books.repository.js';

const etagOf = (b: Book) => `"book-${b.id}-v${b.version}"`;
const isBookCursor = (v: unknown): v is { title: string; id: number } =>
  typeof v === 'object' && v !== null && typeof (v as { title: unknown }).title === 'string' && Number.isInteger((v as { id: unknown }).id);

@Controller('books')
export class BooksController {
  constructor(private readonly books: BooksRepository) {}

  @Get()
  async list(@Query() query: BookListQuery) {
    const after = query.after ? decodeCursor(query.after, isBookCursor) : null;
    const rows = await this.books.search(query.q?.trim() || null, after, query.limit + 1);
    const items = rows.slice(0, query.limit);
    const last = items.at(-1);
    return { items, nextCursor: rows.length > query.limit && last ? encodeCursor({ title: last.title, id: last.id }) : null, limit: query.limit };
  }

  @Post()
  async create(@Body() body: CreateBookRequest, @Res({ passthrough: true }) res: Response) {
    const book = await this.books.create({ ...body, year: body.year ?? null });
    res.status(201).location(`/books/${book.id}`).set('ETag', etagOf(book));
    return book;
  }

  @Get(':id')
  async find(@Param('id', ParseIdPipe) id: number, @Headers('if-none-match') ifNoneMatch: string | undefined, @Res() res: Response) {
    const book = await this.books.find(id);
    if (!book) throw notFound('Book', id);
    res.set('ETag', etagOf(book));
    if (ifNoneMatch === etagOf(book)) return res.status(304).end();
    return res.json(book);
  }

  @Patch(':id')
  async update(
    @Param('id', ParseIdPipe) id: number,
    @Body() body: UpdateBookRequest,
    @Headers('if-match') ifMatch: string | undefined,
    @Res({ passthrough: true }) res: Response,
  ) {
    const changes = Object.fromEntries(Object.entries(body).filter(([, v]) => v !== undefined));
    if (Object.keys(changes).length === 0) throw validationFailed({ body: ['at least one field is required'] });
    const book = await this.books.update(id, changes, expectedVersion(ifMatch, id));
    res.set('ETag', etagOf(book));
    return book;
  }

  @Post(':id/copies')
  @HttpCode(201)
  addCopy(@Param('id', ParseIdPipe) id: number, @Body() body: CreateCopyRequest) {
    return this.books.addCopy(id, body.barcode);
  }
}

/** If-Match: "book-{id}-v{n}" → n. A header that names another book or version never matches. */
function expectedVersion(header: string | undefined, id: number): number | null {
  if (!header) return null;
  const m = /^"book-(\d+)-v(\d+)"$/.exec(header.trim());
  return m && Number(m[1]) === id ? Number(m[2]) : -1;
}

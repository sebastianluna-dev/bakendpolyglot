import { Module } from '@nestjs/common';
import { BooksController } from './books.controller.js';
import { BooksRepository } from './books.repository.js';

@Module({ controllers: [BooksController], providers: [BooksRepository] })
export class BooksModule {}

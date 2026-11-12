import { Body, Controller, Inject, Injectable, Module, Post, Res } from '@nestjs/common';
import { IsEmail, IsString, Length, MaxLength } from 'class-validator';
import type { Response } from 'express';
import type pg from 'pg';
import { POOL, isUniqueViolation } from '../database.module.js';
import { duplicateEmail } from '../domain/errors.js';
import type { Member } from '../domain/types.js';

export class CreateMemberRequest {
  @IsEmail() @MaxLength(254) email: string;
  @IsString() @Length(1, 200) name: string;
}

@Injectable()
export class MembersRepository {
  constructor(@Inject(POOL) private readonly db: pg.Pool) {}

  async create(email: string, name: string): Promise<Member> {
    try {
      return (await this.db.query<Member>('INSERT INTO members (email, name) VALUES ($1, $2) RETURNING id, email, name', [email, name])).rows[0]!;
    } catch (err) {
      if (isUniqueViolation(err, 'members_email_key')) throw duplicateEmail(email);
      throw err;
    }
  }
}

@Controller('members')
export class MembersController {
  constructor(private readonly members: MembersRepository) {}

  @Post()
  async create(@Body() body: CreateMemberRequest, @Res({ passthrough: true }) res: Response) {
    const member = await this.members.create(body.email, body.name);
    res.status(201).location(`/members/${member.id}`);
    return member;
  }
}

@Module({ controllers: [MembersController], providers: [MembersRepository] })
export class MembersModule {}

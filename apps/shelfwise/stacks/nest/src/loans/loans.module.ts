import { Body, Controller, Get, HttpCode, Module, Param, Post, Query, Res } from '@nestjs/common';
import { Type } from 'class-transformer';
import { IsIn, IsInt, IsOptional, IsString, Max, Min } from 'class-validator';
import type { Response } from 'express';
import { ParseIdPipe } from '../common/validation.js';
import { CONFIG, type Config } from '../config.js';
import { decodeCursor, encodeCursor } from '../domain/cursor.js';
import { notFound } from '../domain/errors.js';
import { LoanService } from '../domain/loans.js';
import { LoansRepository } from './loans.repository.js';

export class CreateLoanRequest {
  @IsInt() @Min(1) copyId: number;
  @IsInt() @Min(1) memberId: number;
}

export class MemberLoansQuery {
  @IsOptional() @IsIn(['open', 'all']) status: 'open' | 'all' = 'all';
  @IsOptional() @IsString() after?: string;
  @Type(() => Number) @IsInt() @Min(1) @Max(100) limit: number = 20;
}

const isIdCursor = (v: unknown): v is { id: number } =>
  typeof v === 'object' && v !== null && Number.isInteger((v as { id: unknown }).id);

@Controller()
export class LoansController {
  constructor(
    private readonly service: LoanService,
    private readonly loans: LoansRepository,
  ) {}

  @Post('loans')
  async lend(@Body() body: CreateLoanRequest, @Res({ passthrough: true }) res: Response) {
    const loan = await this.service.lend(body.copyId, body.memberId);
    res.status(201).location(`/loans/${loan.id}`);
    return loan;
  }

  @Post('loans/:id/return')
  @HttpCode(200)
  returnLoan(@Param('id', ParseIdPipe) id: number) {
    return this.service.returnLoan(id);
  }

  @Get('members/:id/loans')
  async forMember(@Param('id', ParseIdPipe) memberId: number, @Query() query: MemberLoansQuery) {
    if (!(await this.loans.memberExists(memberId))) throw notFound('Member', memberId);
    const before = query.after ? decodeCursor(query.after, isIdCursor).id : null;
    const rows = await this.loans.forMember(memberId, query.status === 'open', before, query.limit + 1);
    const items = rows.slice(0, query.limit);
    const last = items.at(-1);
    return { items, nextCursor: rows.length > query.limit && last ? encodeCursor({ id: last.id }) : null, limit: query.limit };
  }
}

@Module({
  controllers: [LoansController],
  providers: [
    LoansRepository,
    // LoanService is plain TypeScript with no decorators; a factory provider builds it from its port and rules.
    {
      provide: LoanService,
      inject: [LoansRepository, CONFIG],
      useFactory: (repo: LoansRepository, c: Config) =>
        new LoanService(repo, { loanDays: c.LOAN_DAYS, maxLoansPerMember: c.MAX_LOANS_PER_MEMBER }),
    },
  ],
})
export class LoansModule {}

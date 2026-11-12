// Input DTOs (Chapter 13): class-validator rules, checked by the global ValidationPipe.
import { Type } from 'class-transformer';
import { IsInt, IsOptional, IsString, Length, Matches, Max, Min, ValidateIf } from 'class-validator';

const THIS_YEAR = new Date().getFullYear();

export class CreateBookRequest {
  @IsString() @Length(1, 200) title: string;
  @IsString() @Length(1, 200) author: string;
  @IsString() @Matches(/^97[89]\d{10}$/, { message: 'must be an ISBN-13 starting with 978 or 979' }) isbn: string;
  @IsOptional() @IsInt() @Min(1450) @Max(THIS_YEAR, { message: 'cannot be in the future' }) year?: number | null;
}

export class UpdateBookRequest {
  // ValidateIf instead of IsOptional: a field may be absent, but title and author may not be null
  @ValidateIf((o) => o.title !== undefined) @IsString() @Length(1, 200) title?: string;
  @ValidateIf((o) => o.author !== undefined) @IsString() @Length(1, 200) author?: string;
  @IsOptional() @IsInt() @Min(1450) @Max(THIS_YEAR, { message: 'cannot be in the future' }) year?: number | null;
}

export class CreateCopyRequest {
  @IsString() @Length(1, 40) barcode: string;
}

export class BookListQuery {
  @IsOptional() @IsString() q?: string;
  @IsOptional() @IsString() after?: string;
  @Type(() => Number) @IsInt() @Min(1) @Max(100) limit: number = 20;
}

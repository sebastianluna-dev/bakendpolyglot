import { Type } from 'class-transformer';
import { ArrayMaxSize, ArrayMinSize, IsArray, IsInt, IsISO8601, IsString, Length, Max, Min, ValidateNested } from 'class-validator';

export class SectionDto {
  @IsString() @Length(1, 40) name: string;
  @IsInt() @Min(1) @Max(26) rows: number;
  @IsInt() @Min(1) @Max(50) seatsPerRow: number;
  @IsInt() @Min(0) @Max(1_000_000) priceCents: number;
}

export class CreateEventDto {
  @IsString() @Length(1, 200) name: string;
  @IsString() @Length(1, 200) venue: string;
  @IsISO8601({ strict: true }) startsAt: string;

  @IsArray()
  @ArrayMinSize(1)
  @ArrayMaxSize(10)
  @ValidateNested({ each: true })
  @Type(() => SectionDto)
  sections: SectionDto[];
}

import { ArrayMinSize, ArrayUnique, IsArray, IsUUID } from 'class-validator';

export class CreateHoldDto {
  @IsUUID('all') eventId: string;

  @IsArray()
  @ArrayMinSize(1)
  @ArrayUnique()
  @IsUUID('all', { each: true })
  seatIds: string[];
}

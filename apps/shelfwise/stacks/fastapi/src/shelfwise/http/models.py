"""Input DTOs (Chapter 13): Pydantic models parse untrusted JSON into typed values, or fail with 422."""

from datetime import date
from typing import Annotated

from pydantic import BaseModel, ConfigDict, EmailStr, Field, field_validator, model_validator

Title = Annotated[str, Field(min_length=1, max_length=200)]
Author = Annotated[str, Field(min_length=1, max_length=200)]
Year = Annotated[int | None, Field(ge=1450)]


def _not_in_future(year: int | None) -> int | None:
    if year is not None and year > date.today().year:
        raise ValueError("cannot be in the future")
    return year


class StrictModel(BaseModel):
    model_config = ConfigDict(extra="forbid")   # unknown fields are rejected: no mass assignment


class CreateBookRequest(StrictModel):
    title: Title
    author: Author
    isbn: Annotated[str, Field(pattern=r"^97[89]\d{10}$")]
    year: Year = None

    _year = field_validator("year")(_not_in_future)


class UpdateBookRequest(StrictModel):
    title: Title | None = None
    author: Author | None = None
    year: Year = None

    _year = field_validator("year")(_not_in_future)

    @model_validator(mode="after")
    def at_least_one_field(self) -> "UpdateBookRequest":
        if not self.model_fields_set:
            raise ValueError("at least one field is required")
        for field in ("title", "author"):
            if field in self.model_fields_set and getattr(self, field) is None:
                raise ValueError(f"{field} cannot be null")
        return self


class CreateCopyRequest(StrictModel):
    barcode: Annotated[str, Field(min_length=1, max_length=40)]


class CreateMemberRequest(StrictModel):
    email: Annotated[EmailStr, Field(max_length=254)]
    name: Annotated[str, Field(min_length=1, max_length=200)]


class CreateLoanRequest(StrictModel):
    copyId: Annotated[int, Field(gt=0)]
    memberId: Annotated[int, Field(gt=0)]

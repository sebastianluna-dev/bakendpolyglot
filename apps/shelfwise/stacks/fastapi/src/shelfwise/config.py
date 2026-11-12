"""Typed configuration, validated at startup (Chapter 16)."""

from typing import Annotated

from pydantic import Field
from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    model_config = SettingsConfigDict(env_file=".env", extra="ignore")

    database_url: str
    port: Annotated[int, Field(ge=1, le=65535)] = 3000
    loan_days: Annotated[int, Field(ge=1, le=60)] = 21
    max_loans_per_member: Annotated[int, Field(ge=1, le=50)] = 5

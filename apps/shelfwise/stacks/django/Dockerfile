FROM python:3.13-slim AS build
COPY --from=ghcr.io/astral-sh/uv:latest /uv /usr/local/bin/uv
WORKDIR /app
ENV UV_COMPILE_BYTECODE=1 UV_LINK_MODE=copy
COPY pyproject.toml uv.lock .python-version ./
RUN uv sync --frozen --no-dev
COPY . .

FROM python:3.13-slim
WORKDIR /app
COPY --from=build /app /app
ENV PATH="/app/.venv/bin:$PATH"
RUN useradd --system app
USER app
EXPOSE 3000
# WSGI with threads: Django's ORM is synchronous, so concurrency comes from workers and threads (Chapter 8)
CMD ["gunicorn", "shelfwise.wsgi", "--workers", "2", "--threads", "8", "--bind", "0.0.0.0:3000"]

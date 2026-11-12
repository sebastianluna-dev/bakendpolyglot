# Same targets in every stack, so one CI workflow drives them all (Chapter 42).
.PHONY: install lint build test run
install: ; uv sync --frozen
lint: install ; uv run ruff check .
build: install
# Settings need DATABASE_URL at import time; the unit tests never connect.
test: install ; DATABASE_URL=$${DATABASE_URL:-postgres://unused:unused@localhost:5432/unused} uv run pytest -q
run: install ; uv run gunicorn shelfwise.wsgi --workers 2 --threads 8 --bind 0.0.0.0:3000

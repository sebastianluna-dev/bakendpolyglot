# Same targets in every stack, so one CI workflow drives them all (Chapter 42).
.PHONY: install lint build test run
install: ; uv sync --frozen
lint: install ; uv run ruff check src tests
build: install
test: install ; uv run pytest -q
run: install ; uv run uvicorn --factory shelfwise.main:create_app --app-dir src --port 3000

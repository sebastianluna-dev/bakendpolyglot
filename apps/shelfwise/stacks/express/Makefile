# Same targets in every stack, so one CI workflow drives them all (Chapter 42).
.PHONY: install lint build test run
install: ; npm ci
lint: install ; npx tsc --noEmit
build: install ; npm run build
test: install ; npx vitest run
run: build ; node dist/main.js

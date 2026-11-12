# Same targets in every stack, so one CI workflow drives them all (Chapter 42).
# `prisma generate` writes the typed client to src/generated (git-ignored).
.PHONY: install lint build test run
install: ; npm ci && npx prisma generate
lint: install ; npx tsc --noEmit -p tsconfig.json
build: install ; npm run build
test: install ; npx vitest run
run: build ; node dist/main.js

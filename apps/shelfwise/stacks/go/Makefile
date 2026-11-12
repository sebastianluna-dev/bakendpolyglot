# Same targets in every stack, so one CI workflow drives them all (Chapter 42).
# `go mod tidy` writes go.sum on first install; commit it after your first local run.
.PHONY: install lint build test run dev
install: ; go mod tidy
lint: install ; gofmt -l . | (! grep .) && go vet ./...
build: install ; CGO_ENABLED=0 go build -o bin/shelfwise ./cmd/shelfwise
test: install ; go test -race ./...
run: build ; ./bin/shelfwise
dev: ; go run ./cmd/shelfwise-mem

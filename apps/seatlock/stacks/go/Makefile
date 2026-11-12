# Same targets in every stack, so one CI workflow drives them all (Chapter 42).
# `go mod tidy` writes go.sum on first install; commit it after your first local run.
.PHONY: install lint build test run
install: ; go mod tidy
lint: install ; gofmt -l . | (! grep .) && go vet ./...
build: install ; CGO_ENABLED=0 go build -o bin/seatlock ./cmd/seatlock
test: install ; go test -race ./...
run: build ; ./bin/seatlock

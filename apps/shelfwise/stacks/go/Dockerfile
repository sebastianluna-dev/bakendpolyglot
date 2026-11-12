FROM golang:1.24 AS build
WORKDIR /src
COPY go.mod go.sum* ./
COPY . .
RUN go mod tidy && CGO_ENABLED=0 go build -trimpath -ldflags="-s -w" -o /out/shelfwise ./cmd/shelfwise

# Distroless: no shell, no package manager, runs as nonroot. A static binary needs nothing else.
FROM gcr.io/distroless/static-debian12:nonroot
COPY --from=build /out/shelfwise /shelfwise
EXPOSE 3000
ENTRYPOINT ["/shelfwise"]

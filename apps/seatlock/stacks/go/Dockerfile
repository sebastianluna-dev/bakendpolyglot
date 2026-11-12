FROM golang:1.24 AS build
WORKDIR /src
COPY . .
RUN go mod tidy && CGO_ENABLED=0 go build -trimpath -ldflags="-s -w" -o /out/seatlock ./cmd/seatlock

FROM gcr.io/distroless/static-debian12:nonroot
COPY --from=build /out/seatlock /seatlock
EXPOSE 3000
ENTRYPOINT ["/seatlock"]

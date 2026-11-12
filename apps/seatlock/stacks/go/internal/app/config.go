// Package app holds typed config and the server lifecycle shared by the binary.
package app

import (
	"context"
	"errors"
	"fmt"
	"log/slog"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"strings"
	"syscall"
	"time"
)

type Config struct {
	Port            int
	DatabaseURL     string
	Issuer          string
	JWKSURL         string
	Audience        string
	RedisURL        string
	PaymentsURL     string
	WebhookSecret   string
	HoldTTL         time.Duration
	ReaperInterval  time.Duration
	MaxSeatsPerHold int
}

// LoadConfig reads the environment and reports every problem at once (Chapter 16).
func LoadConfig(getenv func(string) string) (Config, error) {
	var problems []string
	num := func(key string, def, min, max int) int {
		raw := getenv(key)
		if raw == "" {
			return def
		}
		n, err := strconv.Atoi(raw)
		if err != nil || n < min || n > max {
			problems = append(problems, fmt.Sprintf("%s: must be an integer between %d and %d", key, min, max))
		}
		return n
	}
	need := func(key string) string {
		v := getenv(key)
		if v == "" {
			problems = append(problems, key+": is required")
		}
		return strings.TrimRight(v, "/")
	}
	c := Config{
		Port:            num("PORT", 3000, 1, 65535),
		DatabaseURL:     need("DATABASE_URL"),
		Issuer:          need("OIDC_ISSUER"),
		JWKSURL:         getenv("OIDC_JWKS_URL"),
		Audience:        getenv("OIDC_AUDIENCE"),
		RedisURL:        getenv("REDIS_URL"),
		PaymentsURL:     need("PAYMENTS_URL"),
		WebhookSecret:   need("PAYMENT_WEBHOOK_SECRET"),
		HoldTTL:         time.Duration(num("HOLD_TTL_SECONDS", 600, 1, 3600)) * time.Second,
		ReaperInterval:  time.Duration(num("REAPER_INTERVAL_SECONDS", 60, 0, 3600)) * time.Second,
		MaxSeatsPerHold: num("MAX_SEATS_PER_HOLD", 6, 1, 20),
	}
	if c.JWKSURL == "" {
		c.JWKSURL = c.Issuer + "/protocol/openid-connect/certs"
	}
	if c.Audience == "" {
		c.Audience = "seatlock-api"
	}
	if len(problems) > 0 {
		return c, errors.New("invalid configuration:\n  " + strings.Join(problems, "\n  "))
	}
	return c, nil
}

// Healthcheck lets a distroless image check itself: `seatlock -healthcheck`.
func Healthcheck(port string) int {
	client := http.Client{Timeout: 2 * time.Second}
	res, err := client.Get("http://127.0.0.1:" + port + "/health")
	if err != nil || res.StatusCode != http.StatusOK {
		return 1
	}
	return 0
}

// Serve runs the server until SIGINT/SIGTERM, then drains in-flight requests (Chapter 41).
// background runs with a context that is cancelled at shutdown.
func Serve(port int, h http.Handler, background func(ctx context.Context)) error {
	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()
	if background != nil {
		go background(ctx)
	}
	srv := &http.Server{Addr: fmt.Sprintf(":%d", port), Handler: h, ReadHeaderTimeout: 5 * time.Second,
		ReadTimeout: 15 * time.Second, WriteTimeout: 30 * time.Second, IdleTimeout: 60 * time.Second}
	errs := make(chan error, 1)
	go func() { errs <- srv.ListenAndServe() }()
	slog.Info("listening", "port", port)
	select {
	case err := <-errs:
		return err
	case <-ctx.Done():
	}
	shutdownCtx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	return srv.Shutdown(shutdownCtx)
}

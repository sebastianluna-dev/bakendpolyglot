// Package app holds what both binaries share: typed config, logging and a server that shuts down cleanly.
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
	Port              int
	DatabaseURL       string
	LoanDays          int
	MaxLoansPerMember int
	LogLevel          slog.Level
}

// LoadConfig reads the environment and reports every problem at once (Chapter 16).
func LoadConfig(getenv func(string) string, needDatabase bool) (Config, error) {
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
	c := Config{
		Port:              num("PORT", 3000, 1, 65535),
		DatabaseURL:       getenv("DATABASE_URL"),
		LoanDays:          num("LOAN_DAYS", 21, 1, 60),
		MaxLoansPerMember: num("MAX_LOANS_PER_MEMBER", 5, 1, 50),
	}
	if needDatabase && c.DatabaseURL == "" {
		problems = append(problems, "DATABASE_URL: is required")
	}
	if err := c.LogLevel.UnmarshalText([]byte(strings.ToUpper(orDefault(getenv("LOG_LEVEL"), "info")))); err != nil {
		problems = append(problems, "LOG_LEVEL: must be debug, info, warn or error")
	}
	if len(problems) > 0 {
		return c, errors.New("invalid configuration:\n  " + strings.Join(problems, "\n  "))
	}
	return c, nil
}

func orDefault(v, def string) string {
	if v == "" {
		return def
	}
	return v
}

func Logger(level slog.Level) *slog.Logger {
	return slog.New(slog.NewJSONHandler(os.Stdout, &slog.HandlerOptions{Level: level}))
}

// Healthcheck lets a distroless image check itself: `shelfwise -healthcheck` (there is no curl inside).
func Healthcheck(port int) int {
	client := http.Client{Timeout: 2 * time.Second}
	res, err := client.Get(fmt.Sprintf("http://127.0.0.1:%d/health", port))
	if err != nil || res.StatusCode != http.StatusOK {
		return 1
	}
	return 0
}

// Serve runs the server until SIGINT/SIGTERM, then drains in-flight requests (Chapter 41).
func Serve(port int, h http.Handler) error {
	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	srv := &http.Server{
		Addr:              fmt.Sprintf(":%d", port),
		Handler:           h,
		ReadHeaderTimeout: 5 * time.Second,
		ReadTimeout:       15 * time.Second,
		WriteTimeout:      15 * time.Second,
		IdleTimeout:       60 * time.Second,
	}
	errs := make(chan error, 1)
	go func() { errs <- srv.ListenAndServe() }()
	slog.Info("listening", "port", port)

	select {
	case err := <-errs:
		return err
	case <-ctx.Done():
	}
	slog.Info("shutting down")
	shutdownCtx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	return srv.Shutdown(shutdownCtx)
}

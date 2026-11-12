// Command seatlock is the SeatLock API on net/http, pgx, golang-jwt and go-redis.
package main

import (
	"context"
	"flag"
	"fmt"
	"log/slog"
	"os"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/app"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/auth"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/cache"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/domain"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/httpapi"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/postgres"
)

func main() {
	health := flag.Bool("healthcheck", false, "probe /health and exit")
	flag.Parse()
	if *health {
		port := os.Getenv("PORT")
		if port == "" {
			port = "3000"
		}
		os.Exit(app.Healthcheck(port))
	}
	slog.SetDefault(slog.New(slog.NewJSONHandler(os.Stdout, nil)))
	if err := run(); err != nil {
		slog.Error("stopped", "err", err)
		os.Exit(1)
	}
}

func run() error {
	cfg, err := app.LoadConfig(os.Getenv)
	if err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(2)
	}
	ctx, cancel := context.WithTimeout(context.Background(), 15*time.Second)
	defer cancel()

	pool, err := pgxpool.New(ctx, cfg.DatabaseURL)
	if err == nil {
		err = pool.Ping(ctx)
	}
	if err != nil {
		return fmt.Errorf("database: %w", err)
	}
	defer pool.Close()

	verifier, err := auth.New(context.Background(), cfg.JWKSURL, cfg.Issuer, cfg.Audience)
	if err != nil {
		return fmt.Errorf("jwks: %w", err)
	}

	var c domain.Cache = httpapi.NoCache{}
	if cfg.RedisURL != "" {
		r, err := cache.New(cfg.RedisURL)
		if err != nil {
			return fmt.Errorf("redis: %w", err)
		}
		defer r.Close()
		c = r
	}

	store := postgres.New(pool)
	handler := httpapi.NewHandler(httpapi.Deps{
		Store:    store,
		Verifier: verifier,
		Cache:    c,
		Payments: httpapi.NewPaymentsClient(cfg.PaymentsURL),
		Config:   httpapi.Config{HoldTTL: cfg.HoldTTL, MaxSeatsPerHold: cfg.MaxSeatsPerHold, WebhookSecret: []byte(cfg.WebhookSecret)},
	})
	return app.Serve(cfg.Port, handler, func(ctx context.Context) { reaper(ctx, store, cfg.ReaperInterval) })
}

// reaper releases expired holds on a ticker (Chapter 24); River or a CronJob would also do.
func reaper(ctx context.Context, store *postgres.Store, every time.Duration) {
	if every == 0 {
		return
	}
	t := time.NewTicker(every)
	defer t.Stop()
	for {
		select {
		case <-ctx.Done():
			return
		case <-t.C:
			if n, err := store.ReleaseExpiredHolds(ctx, time.Now()); err != nil {
				slog.Error("reaper run failed", "err", err)
			} else if n > 0 {
				slog.Info("released expired holds", "count", n)
			}
		}
	}
}

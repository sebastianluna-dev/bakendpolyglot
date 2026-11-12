// Command shelfwise is the production binary: Postgres via pgx.
package main

import (
	"context"
	"flag"
	"fmt"
	"log/slog"
	"os"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/app"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/domain"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/httpapi"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/postgres"
)

func main() {
	health := flag.Bool("healthcheck", false, "probe /health and exit")
	flag.Parse()

	cfg, err := app.LoadConfig(os.Getenv, !*health)
	if err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(2)
	}
	if *health {
		os.Exit(app.Healthcheck(cfg.Port))
	}
	slog.SetDefault(app.Logger(cfg.LogLevel))

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	pool, err := pgxpool.New(ctx, cfg.DatabaseURL)
	if err == nil {
		err = pool.Ping(ctx)
	}
	if err != nil {
		slog.Error("database unavailable", "err", err)
		os.Exit(1)
	}
	defer pool.Close()

	store := postgres.New(pool)
	handler := httpapi.NewHandler(httpapi.Deps{
		Books:   store.Books(),
		Members: store.Members(),
		Loans:   store.Loans(),
		Lending: domain.NewLoanService(store.Lending(),
			domain.LoanRules{LoanDays: cfg.LoanDays, MaxLoansPerMember: cfg.MaxLoansPerMember}, nil),
	})
	if err := app.Serve(cfg.Port, handler); err != nil {
		slog.Error("server stopped", "err", err)
		os.Exit(1)
	}
}

// Command shelfwise-mem runs the same HTTP layer on the in-memory store: no database, no
// dependencies outside the standard library. Data is lost on exit. Handy for front-end work and demos.
package main

import (
	"fmt"
	"log/slog"
	"os"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/app"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/domain"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/httpapi"
	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/memstore"
)

func main() {
	cfg, err := app.LoadConfig(os.Getenv, false)
	if err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(2)
	}
	slog.SetDefault(app.Logger(cfg.LogLevel))
	store := memstore.New()
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

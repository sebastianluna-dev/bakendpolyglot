package domain

import (
	"context"
	"time"
)

// LoanStore is the port the loan rules need. The postgres package implements it; tests use a fake.
type LoanStore interface {
	MemberExists(ctx context.Context, memberID int64) (bool, error)
	CopyExists(ctx context.Context, copyID int64) (bool, error)
	CopyHasOpenLoan(ctx context.Context, copyID int64) (bool, error)
	CountOpenLoans(ctx context.Context, memberID int64) (int, error)
	// InsertLoan must return CopyOnLoan if the database rejects a second open loan for the copy.
	InsertLoan(ctx context.Context, copyID, memberID int64, loanedAt, dueAt time.Time) (Loan, error)
	ReturnLoan(ctx context.Context, loanID int64, returnedAt time.Time) (Loan, error)
}

type LoanRules struct {
	LoanDays          int
	MaxLoansPerMember int
}

type LoanService struct {
	store LoanStore
	rules LoanRules
	now   func() time.Time
}

func NewLoanService(store LoanStore, rules LoanRules, now func() time.Time) *LoanService {
	if now == nil {
		now = time.Now
	}
	return &LoanService{store: store, rules: rules, now: now}
}

func (s *LoanService) Lend(ctx context.Context, copyID, memberID int64) (Loan, error) {
	if ok, err := s.store.CopyExists(ctx, copyID); err != nil {
		return Loan{}, err
	} else if !ok {
		return Loan{}, NotFound("Copy", copyID)
	}
	if ok, err := s.store.MemberExists(ctx, memberID); err != nil {
		return Loan{}, err
	} else if !ok {
		return Loan{}, NotFound("Member", memberID)
	}
	// Friendly checks first; the partial unique index still settles races (Chapter 23).
	if onLoan, err := s.store.CopyHasOpenLoan(ctx, copyID); err != nil {
		return Loan{}, err
	} else if onLoan {
		return Loan{}, CopyOnLoan(copyID)
	}
	if open, err := s.store.CountOpenLoans(ctx, memberID); err != nil {
		return Loan{}, err
	} else if open >= s.rules.MaxLoansPerMember {
		return Loan{}, LoanLimit(memberID, s.rules.MaxLoansPerMember)
	}

	loanedAt := s.now().UTC()
	return s.store.InsertLoan(ctx, copyID, memberID, loanedAt, loanedAt.AddDate(0, 0, s.rules.LoanDays))
}

func (s *LoanService) Return(ctx context.Context, loanID int64) (Loan, error) {
	return s.store.ReturnLoan(ctx, loanID, s.now().UTC())
}

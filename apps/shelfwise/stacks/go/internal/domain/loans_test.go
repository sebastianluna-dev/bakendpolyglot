package domain

import (
	"context"
	"errors"
	"testing"
	"time"
)

// fakeStore is a ten-line implementation of the port: Go needs no mocking library (Chapter 7).
type fakeStore struct {
	open    map[int64]int64 // copyID → memberID
	copies  map[int64]bool
	members map[int64]bool
}

func newFake() *fakeStore {
	return &fakeStore{open: map[int64]int64{}, copies: map[int64]bool{1: true, 2: true, 3: true}, members: map[int64]bool{10: true}}
}

func (f *fakeStore) MemberExists(_ context.Context, id int64) (bool, error) {
	return f.members[id], nil
}
func (f *fakeStore) CopyExists(_ context.Context, id int64) (bool, error) { return f.copies[id], nil }
func (f *fakeStore) CopyHasOpenLoan(_ context.Context, id int64) (bool, error) {
	_, ok := f.open[id]
	return ok, nil
}
func (f *fakeStore) CountOpenLoans(_ context.Context, memberID int64) (int, error) {
	n := 0
	for _, m := range f.open {
		if m == memberID {
			n++
		}
	}
	return n, nil
}
func (f *fakeStore) InsertLoan(_ context.Context, copyID, memberID int64, loanedAt, dueAt time.Time) (Loan, error) {
	if _, ok := f.open[copyID]; ok {
		return Loan{}, CopyOnLoan(copyID)
	}
	f.open[copyID] = memberID
	return Loan{ID: int64(len(f.open)), CopyID: copyID, MemberID: memberID, LoanedAt: loanedAt, DueAt: dueAt}, nil
}
func (f *fakeStore) ReturnLoan(context.Context, int64, time.Time) (Loan, error) {
	return Loan{}, errors.New("not used")
}

func jan31() time.Time { return time.Date(2026, 1, 31, 10, 0, 0, 0, time.UTC) }

func code(err error) string {
	var de *Error
	if errors.As(err, &de) {
		return de.Code
	}
	return ""
}

func TestLend(t *testing.T) {
	tests := []struct {
		name     string
		setup    func(*fakeStore)
		maxLoans int
		copyID   int64
		memberID int64
		wantCode string
	}{
		{name: "ok", maxLoans: 5, copyID: 1, memberID: 10},
		{name: "copy on loan", setup: func(f *fakeStore) { f.open[1] = 99 }, maxLoans: 5, copyID: 1, memberID: 10, wantCode: "copy-on-loan"},
		{name: "limit reached", setup: func(f *fakeStore) { f.open[2] = 10; f.open[3] = 10 }, maxLoans: 2, copyID: 1, memberID: 10, wantCode: "loan-limit"},
		{name: "unknown copy", maxLoans: 5, copyID: 99, memberID: 10, wantCode: "not-found"},
		{name: "unknown member", maxLoans: 5, copyID: 1, memberID: 99, wantCode: "not-found"},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			store := newFake()
			if tt.setup != nil {
				tt.setup(store)
			}
			svc := NewLoanService(store, LoanRules{LoanDays: 21, MaxLoansPerMember: tt.maxLoans}, jan31)
			loan, err := svc.Lend(context.Background(), tt.copyID, tt.memberID)
			if got := code(err); got != tt.wantCode {
				t.Fatalf("got code %q (err %v), want %q", got, err, tt.wantCode)
			}
			if tt.wantCode == "" && !loan.DueAt.Equal(time.Date(2026, 2, 21, 10, 0, 0, 0, time.UTC)) {
				t.Fatalf("due date %v, want 2026-02-21 across the month end", loan.DueAt)
			}
		})
	}
}

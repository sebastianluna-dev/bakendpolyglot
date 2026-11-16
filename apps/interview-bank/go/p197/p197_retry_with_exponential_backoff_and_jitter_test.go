package p197

import (
	"fmt"
	"slices"
	"testing"
)

func flaky(failures int, result string) func() (string, error) {
	calls := 0
	return func() (string, error) {
		calls++
		if calls <= failures {
			return "", fmt.Errorf("timeout %d", calls)
		}
		return result, nil
	}
}

func cycle(values ...float64) func() float64 {
	calls := 0
	return func() float64 {
		calls++
		return values[(calls-1)%len(values)]
	}
}

func TestRetry(t *testing.T) {
	cases := []struct {
		name                            string
		failures                        int
		maxAttempts, baseDelayMs, capMs int
		random                          []float64
		want, wantErr                   string
		wantDelays                      []int
	}{
		{"succeeds on the third attempt", 2, 5, 100, 1000, []float64{0.5}, "receipt-42", "", []int{50, 100}},
		{"every attempt fails", 10, 4, 100, 250, []float64{0.5, 0.25, 0.75}, "", "timeout 4", []int{50, 50, 187}},
		{"succeeds at once", 0, 3, 100, 1000, []float64{0.5}, "first try", "", []int{}},
		{"one attempt allowed", 1, 1, 100, 1000, []float64{0.5}, "", "timeout 1", []int{}},
		{"delays reach the cap", 6, 7, 1000, 5000, []float64{0.999}, "ok", "", []int{999, 1998, 3996, 4995, 4995, 4995}},
	}
	for _, c := range cases {
		delays := []int{}
		record := func(ms int) { delays = append(delays, ms) }
		got, err := Retry(flaky(c.failures, c.want), c.maxAttempts, c.baseDelayMs, c.capMs, record, cycle(c.random...))
		gotErr := ""
		if err != nil {
			gotErr = err.Error()
		}
		if got != c.want || gotErr != c.wantErr || !slices.Equal(delays, c.wantDelays) {
			t.Errorf("%s: got %q, error %q, delays %v; want %q, error %q, delays %v",
				c.name, got, gotErr, delays, c.want, c.wantErr, c.wantDelays)
		}
	}
}

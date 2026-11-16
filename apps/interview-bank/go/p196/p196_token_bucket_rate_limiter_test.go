package p196

import (
	"slices"
	"testing"
)

func TestTokenBucket(t *testing.T) {
	cases := []struct {
		capacity, refill int
		times            []int64
		want             []bool
	}{
		{3, 1, []int64{0, 0, 0, 0, 999, 1000, 1000, 5000, 5000, 5000, 5000},
			[]bool{true, true, true, false, false, true, false, true, true, true, false}},
		{2, 4, []int64{100, 100, 200, 350, 400, 600}, []bool{true, true, false, true, false, true}},
		{1, 1, []int64{1_000_000_000, 1_000_000_000, 1_000_000_999, 1_000_001_000}, []bool{true, false, false, true}},
		{5, 10_000, []int64{7, 7, 7, 7, 7, 7, 7}, []bool{true, true, true, true, true, false, false}},
	}
	for _, c := range cases {
		b := NewTokenBucket(c.capacity, c.refill)
		got := make([]bool, len(c.times))
		for i, now := range c.times {
			got[i] = b.Allow(now)
		}
		if !slices.Equal(got, c.want) {
			t.Errorf("TokenBucket(%d, %d) over %v = %v, want %v", c.capacity, c.refill, c.times, got, c.want)
		}
	}
}

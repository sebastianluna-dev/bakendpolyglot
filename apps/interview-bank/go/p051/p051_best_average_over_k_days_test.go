package p051

import (
	"math"
	"testing"
)

func TestBestAverage(t *testing.T) {
	cases := []struct {
		balances []int
		k        int
		want     float64
	}{
		{[]int{4, -2, 7, 5, -1, 6}, 4, 4.25},
		{[]int{-3, -1, -4}, 2, -2.0},
		{[]int{5}, 1, 5.0},
		{[]int{1, 2, 3, 4}, 1, 4.0},
		{[]int{-7, -7, -7}, 3, -7.0},
		{[]int{1, 1, 2}, 3, 4.0 / 3},
	}
	for _, c := range cases {
		if got := BestAverage(c.balances, c.k); math.Abs(got-c.want) >= 1e-9 {
			t.Errorf("BestAverage(%v, %d) = %v, want %v", c.balances, c.k, got, c.want)
		}
	}
}

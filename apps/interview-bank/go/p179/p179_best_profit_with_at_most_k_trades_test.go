package p179

import "testing"

func TestBestProfit(t *testing.T) {
	prices := []int{3, 8, 5, 1, 7, 8}
	zigzag := []int{2, 5, 1, 4, 3, 6}
	cases := []struct {
		prices []int
		k      int
		want   int
	}{
		{prices, 2, 12},
		{prices, 1, 7},
		{[]int{9, 7, 4, 1}, 2, 0},
		{prices, 0, 0},
		{[]int{}, 3, 0},
		{[]int{7}, 1, 0},
		{[]int{1, 2, 3, 4, 5}, 2, 4},
		{zigzag, 1, 5},
		{zigzag, 2, 8},
		{zigzag, 3, 9},
		{zigzag, 10, 9},
		{[]int{1, 3, 2, 8}, 1, 7},
	}
	for _, c := range cases {
		if got := BestProfit(c.prices, c.k); got != c.want {
			t.Errorf("BestProfit(%v, %d) = %d, want %d", c.prices, c.k, got, c.want)
		}
	}
}

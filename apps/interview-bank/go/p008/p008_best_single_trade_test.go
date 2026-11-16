package p008

import "testing"

func TestBestTrade(t *testing.T) {
	cases := []struct {
		prices []int
		want   int
	}{
		{[]int{8, 3, 6, 1, 7, 4}, 6},
		{[]int{9, 7, 4, 2}, 0},
		{[]int{5, 9, 1}, 4},
		{[]int{3}, 0},
		{[]int{}, 0},
	}
	for _, c := range cases {
		if got := BestTrade(c.prices); got != c.want {
			t.Errorf("BestTrade(%v) = %d, want %d", c.prices, got, c.want)
		}
	}
}

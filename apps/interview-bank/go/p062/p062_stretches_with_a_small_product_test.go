package p062

import "testing"

func TestCountSmallProducts(t *testing.T) {
	cases := []struct {
		factors []int
		limit   int
		want    int
	}{
		{[]int{4, 2, 6, 1, 3}, 15, 10},
		{[]int{1, 1, 1}, 1, 0},
		{[]int{1, 1, 1}, 2, 6},
		{[]int{}, 10, 0},
		{[]int{7}, 0, 0},
		{[]int{10, 5, 2}, 100, 5},
		{[]int{1000, 1000}, 1000000, 2},
	}
	for _, c := range cases {
		if got := CountSmallProducts(c.factors, c.limit); got != c.want {
			t.Errorf("CountSmallProducts(%v, %d) = %d, want %d", c.factors, c.limit, got, c.want)
		}
	}
}

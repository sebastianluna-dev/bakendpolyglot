package p011

import "testing"

func TestBalancePoint(t *testing.T) {
	cases := []struct {
		nums []int
		want int
	}{
		{[]int{3, 1, 5, 2, 2}, 2},
		{[]int{1, 2, 3}, -1},
		{[]int{}, -1},
		{[]int{7}, 0},
		{[]int{5, 2, -2}, 0},
		{[]int{0, 0, 0}, 0},
	}
	for _, c := range cases {
		if got := BalancePoint(c.nums); got != c.want {
			t.Errorf("BalancePoint(%v) = %d, want %d", c.nums, got, c.want)
		}
	}
}

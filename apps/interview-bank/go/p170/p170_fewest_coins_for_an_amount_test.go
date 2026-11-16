package p170

import "testing"

func TestFewestCoins(t *testing.T) {
	cases := []struct {
		coins  []int
		amount int
		want   int
	}{
		{[]int{1, 4, 6}, 8, 2},
		{[]int{5, 10}, 3, -1},
		{[]int{7}, 0, 0},
		{[]int{2}, 3, -1},
		{[]int{3, 7}, 20, 4},
		{[]int{2, 5, 10, 25}, 99, 7},
		{[]int{4, 7}, 5000, 716},
	}
	for _, c := range cases {
		if got := FewestCoins(c.coins, c.amount); got != c.want {
			t.Errorf("FewestCoins(%v, %d) = %d, want %d", c.coins, c.amount, got, c.want)
		}
	}
}

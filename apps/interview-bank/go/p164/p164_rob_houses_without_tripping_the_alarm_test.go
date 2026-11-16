package p164

import "testing"

func TestMaxHaul(t *testing.T) {
	cases := []struct {
		cash []int
		want int
	}{
		{[]int{6, 1, 2, 9}, 15},
		{[]int{5, 10, 4, 3, 8}, 18},
		{[]int{}, 0},
		{[]int{7}, 7},
		{[]int{2, 1, 1, 2}, 4},
		{[]int{4, 1, 2, 7, 5, 3, 1}, 14},
		{[]int{0, 0, 0}, 0},
	}
	for _, c := range cases {
		if got := MaxHaul(c.cash); got != c.want {
			t.Errorf("MaxHaul(%v) = %d, want %d", c.cash, got, c.want)
		}
	}
}

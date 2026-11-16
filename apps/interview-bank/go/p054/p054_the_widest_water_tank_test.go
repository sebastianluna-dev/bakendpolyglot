package p054

import "testing"

func TestWidestTank(t *testing.T) {
	cases := []struct {
		heights []int
		want    int
	}{
		{[]int{2, 6, 3, 1, 5, 2}, 15},
		{[]int{4, 4}, 4},
		{[]int{7}, 0},
		{[]int{}, 0},
		{[]int{1, 2, 3, 4, 5}, 6},
		{[]int{0, 0, 0}, 0},
		{[]int{5, 1, 1, 1, 5}, 20},
	}
	for _, c := range cases {
		if got := WidestTank(c.heights); got != c.want {
			t.Errorf("WidestTank(%v) = %d, want %d", c.heights, got, c.want)
		}
	}
}

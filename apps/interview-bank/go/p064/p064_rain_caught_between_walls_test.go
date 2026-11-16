package p064

import "testing"

func TestTrappedRain(t *testing.T) {
	cases := []struct {
		heights []int
		want    int
	}{
		{[]int{3, 0, 2, 0, 4}, 7},
		{[]int{5, 4, 3}, 0},
		{[]int{}, 0},
		{[]int{7}, 0},
		{[]int{2, 0, 2}, 2},
		{[]int{0, 2, 0, 1, 0, 3, 1, 0, 2}, 8},
		{[]int{4, 1, 1, 4}, 6},
		{[]int{3, 0, 1}, 1},
	}
	for _, c := range cases {
		if got := TrappedRain(c.heights); got != c.want {
			t.Errorf("TrappedRain(%v) = %d, want %d", c.heights, got, c.want)
		}
	}
}

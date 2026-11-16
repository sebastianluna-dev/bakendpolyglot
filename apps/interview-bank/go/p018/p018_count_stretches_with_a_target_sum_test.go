package p018

import "testing"

func TestCountStretches(t *testing.T) {
	cases := []struct {
		changes []int
		target  int
		want    int
	}{
		{[]int{1, 2, 3, -2, 2}, 3, 4},
		{[]int{0, 0, 0}, 0, 6},
		{[]int{}, 0, 0},
		{[]int{5}, 3, 0},
		{[]int{3}, 3, 1},
		{[]int{1, -1, 1, -1}, 0, 4},
	}
	for _, c := range cases {
		if got := CountStretches(c.changes, c.target); got != c.want {
			t.Errorf("CountStretches(%v, %d) = %d, want %d", c.changes, c.target, got, c.want)
		}
	}
}

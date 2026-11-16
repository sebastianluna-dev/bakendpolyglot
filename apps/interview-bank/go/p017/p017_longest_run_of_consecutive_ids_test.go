package p017

import "testing"

func TestLongestRun(t *testing.T) {
	cases := []struct {
		ids  []int
		want int
	}{
		{[]int{104, 7, 101, 5, 103, 6, 102}, 4},
		{[]int{9, 1, 9, 2, 2}, 2},
		{[]int{}, 0},
		{[]int{5}, 1},
		{[]int{-1, 0, 1, 3}, 3},
		{[]int{10, 30, 20}, 1},
	}
	for _, c := range cases {
		if got := LongestRun(c.ids); got != c.want {
			t.Errorf("LongestRun(%v) = %d, want %d", c.ids, got, c.want)
		}
	}
}

package p135

import "testing"

func TestShortestClearPath(t *testing.T) {
	cases := []struct {
		grid [][]int
		want int
	}{
		{[][]int{{0, 0, 0}, {1, 1, 0}, {1, 1, 0}}, 4},
		{[][]int{{0, 1}, {1, 0}}, 2},
		{[][]int{{0}}, 1},
		{[][]int{{1}}, -1},
		{[][]int{{1, 0}, {0, 0}}, -1},
		{[][]int{{0, 0}, {0, 1}}, -1},
		{[][]int{{0, 1, 0}, {1, 1, 0}, {0, 0, 0}}, -1},
		{[][]int{{0, 0, 0, 0}}, 4},
		{[][]int{{0, 1, 0, 0}, {0, 1, 0, 1}, {0, 1, 0, 1}, {1, 0, 1, 0}}, 6},
	}
	for _, c := range cases {
		if got := ShortestClearPath(c.grid); got != c.want {
			t.Errorf("ShortestClearPath(%v) = %d, want %d", c.grid, got, c.want)
		}
	}
}

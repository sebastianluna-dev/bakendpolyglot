package p128

import "testing"

func TestCountIslands(t *testing.T) {
	cases := []struct {
		grid [][]int
		want int
	}{
		{[][]int{{1, 1, 0, 0, 1}, {1, 0, 0, 1, 1}, {0, 0, 1, 0, 0}, {1, 0, 0, 0, 1}}, 5},
		{[][]int{{1, 0, 1}, {0, 1, 0}, {1, 0, 1}}, 5},
		{[][]int{{0, 0}, {0, 0}}, 0},
		{[][]int{{1, 1}, {1, 1}}, 1},
		{[][]int{{1, 1, 1}, {0, 0, 1}, {1, 1, 1}}, 1},
		{[][]int{{1}}, 1},
	}
	for _, c := range cases {
		if got := CountIslands(c.grid); got != c.want {
			t.Errorf("CountIslands(%v) = %d, want %d", c.grid, got, c.want)
		}
	}
}

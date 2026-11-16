package p127

import "testing"

func TestIslandPerimeter(t *testing.T) {
	cases := []struct {
		grid [][]int
		want int
	}{
		{[][]int{{0, 1, 0}, {1, 1, 1}, {0, 1, 0}}, 12},
		{[][]int{{1, 1}, {1, 1}}, 8},
		{[][]int{{1}}, 4},
		{[][]int{{0, 0, 0}, {0, 1, 0}}, 4},
		{[][]int{{1, 1, 1, 0}}, 8},
		{[][]int{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}}, 16},
	}
	for _, c := range cases {
		if got := IslandPerimeter(c.grid); got != c.want {
			t.Errorf("IslandPerimeter(%v) = %d, want %d", c.grid, got, c.want)
		}
	}
}

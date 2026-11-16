package p168

import "testing"

func TestMinPaintCost(t *testing.T) {
	cases := []struct {
		costs [][]int
		want  int
	}{
		{[][]int{{8, 3, 6}, {4, 9, 2}, {7, 5, 1}}, 8},
		{[][]int{{1, 2, 9}, {1, 9, 9}}, 3},
		{[][]int{}, 0},
		{[][]int{{5, 1, 9}}, 1},
		{[][]int{{7, 6, 2}, {3, 8, 9}, {4, 1, 6}, {9, 2, 3}}, 9},
		{[][]int{{4, 4, 4}, {4, 4, 4}}, 8},
	}
	for _, c := range cases {
		if got := MinPaintCost(c.costs); got != c.want {
			t.Errorf("MinPaintCost(%v) = %d, want %d", c.costs, got, c.want)
		}
	}
}

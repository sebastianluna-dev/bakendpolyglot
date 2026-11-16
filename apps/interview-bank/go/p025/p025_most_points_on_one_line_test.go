package p025

import "testing"

func TestMaxPointsOnLine(t *testing.T) {
	cases := []struct {
		points [][]int
		want   int
	}{
		{[][]int{{1, 1}, {2, 3}, {3, 5}, {4, 2}, {0, -1}}, 4},
		{[][]int{{3, 4}, {3, 4}, {3, -1}, {0, 0}}, 3},
		{[][]int{}, 0},
		{[][]int{{5, 5}}, 1},
		{[][]int{{1, 1}, {1, 1}, {1, 1}}, 3},
		{[][]int{{0, 0}, {1, 1}, {2, 0}}, 2},
		{[][]int{{-2, 7}, {0, 7}, {9, 7}, {1, 1}}, 3},
		{[][]int{{0, 0}, {1, -1}, {-1, 1}, {2, -2}}, 4},
	}
	for _, c := range cases {
		if got := MaxPointsOnLine(c.points); got != c.want {
			t.Errorf("MaxPointsOnLine(%v) = %d, want %d", c.points, got, c.want)
		}
	}
}

package p131

import "testing"

func TestMinutesUntilAllFail(t *testing.T) {
	cases := []struct {
		grid [][]int
		want int
	}{
		{[][]int{{1, 1, 0, 2}, {0, 1, 1, 1}, {0, 0, 0, 1}}, 5},
		{[][]int{{1, 1, 0, 2}, {0, 1, 1, 1}, {1, 0, 0, 1}}, -1},
		{[][]int{{0, 2}}, 0},
		{[][]int{{0}}, 0},
		{[][]int{{1}}, -1},
		{[][]int{{2, 1}}, 1},
		{[][]int{{2, 1, 1, 1, 2}}, 2},
	}
	for _, c := range cases {
		if got := MinutesUntilAllFail(c.grid); got != c.want {
			t.Errorf("MinutesUntilAllFail(%v) = %d, want %d", c.grid, got, c.want)
		}
	}
}

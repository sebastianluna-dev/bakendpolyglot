package p187

import "testing"

func TestFewestRemovals(t *testing.T) {
	cases := []struct {
		intervals [][]int
		want      int
	}{
		{[][]int{{1, 10}, {2, 3}, {4, 5}, {6, 7}}, 1},
		{[][]int{{1, 3}, {3, 5}, {5, 8}}, 0},
		{[][]int{}, 0},
		{[][]int{{1, 2}, {1, 2}, {1, 2}}, 2},
		{[][]int{{1, 4}, {2, 3}, {3, 6}, {5, 7}}, 2},
	}
	for _, c := range cases {
		if got := FewestRemovals(c.intervals); got != c.want {
			t.Errorf("FewestRemovals(%v) = %d, want %d", c.intervals, got, c.want)
		}
	}
}

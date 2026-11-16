package p140

import "testing"

func TestCheapestConnection(t *testing.T) {
	cases := []struct {
		n      int
		cables [][]int
		want   int
	}{
		{4, [][]int{{0, 1, 3}, {1, 2, 1}, {2, 3, 4}, {0, 2, 2}, {1, 3, 5}}, 7},
		{3, [][]int{{0, 1, 5}}, -1},
		{1, [][]int{}, 0},
		{2, [][]int{}, -1},
		{2, [][]int{{0, 1, 9}, {1, 0, 4}, {0, 0, 1}}, 4},
		{5, [][]int{{0, 1, 2}, {1, 2, 2}, {2, 0, 2}, {3, 4, 1}, {2, 3, 7}, {0, 4, 8}}, 12},
	}
	for _, c := range cases {
		if got := CheapestConnection(c.n, c.cables); got != c.want {
			t.Errorf("CheapestConnection(%d, %v) = %d, want %d", c.n, c.cables, got, c.want)
		}
	}
}

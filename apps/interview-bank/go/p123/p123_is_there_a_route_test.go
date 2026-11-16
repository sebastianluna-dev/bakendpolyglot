package p123

import "testing"

func TestHasRoute(t *testing.T) {
	roads := [][]int{{0, 1}, {1, 3}, {3, 2}, {4, 5}}
	cases := []struct {
		n              int
		edges          [][]int
		source, target int
		want           bool
	}{
		{6, roads, 0, 2, true},
		{6, roads, 0, 5, false},
		{1, [][]int{}, 0, 0, true},
		{3, [][]int{}, 0, 2, false},
		{2, [][]int{{1, 0}}, 0, 1, true},
		{4, [][]int{{0, 1}, {1, 0}, {1, 2}, {2, 0}, {2, 3}}, 3, 0, true},
	}
	for _, c := range cases {
		if got := HasRoute(c.n, c.edges, c.source, c.target); got != c.want {
			t.Errorf("HasRoute(%d, %v, %d, %d) = %v, want %v", c.n, c.edges, c.source, c.target, got, c.want)
		}
	}
}

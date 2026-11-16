package p134

import "testing"

func TestSignalTime(t *testing.T) {
	cases := []struct {
		n      int
		links  [][]int
		source int
		want   int
	}{
		{4, [][]int{{0, 1, 4}, {0, 2, 1}, {2, 1, 2}, {1, 3, 1}, {2, 3, 6}}, 0, 4},
		{3, [][]int{{0, 1, 2}, {2, 1, 1}}, 0, -1},
		{1, [][]int{}, 0, 0},
		{2, [][]int{{0, 1, 5}, {0, 1, 2}}, 0, 2},
		{2, [][]int{{0, 1, 3}}, 1, -1},
		{2, [][]int{{1, 0, 3}}, 1, 3},
		{3, [][]int{{0, 1, 1}, {1, 2, 1}, {2, 0, 1}}, 1, 2},
		{3, [][]int{{0, 1, 0}, {1, 2, 0}}, 0, 0},
	}
	for _, c := range cases {
		if got := SignalTime(c.n, c.links, c.source); got != c.want {
			t.Errorf("SignalTime(%d, %v, %d) = %d, want %d", c.n, c.links, c.source, got, c.want)
		}
	}
}

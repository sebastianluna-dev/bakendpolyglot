package p133

import "testing"

func TestCountSegments(t *testing.T) {
	cases := []struct {
		n     int
		links [][]int
		want  int
	}{
		{5, [][]int{{0, 1}, {1, 2}, {3, 4}}, 2},
		{6, [][]int{{0, 1}, {2, 3}, {1, 0}}, 4},
		{1, [][]int{}, 1},
		{3, [][]int{}, 3},
		{3, [][]int{{0, 1}, {1, 2}, {2, 0}}, 1},
		{2, [][]int{{1, 1}}, 2},
		{6, [][]int{{0, 1}, {2, 3}, {4, 5}, {1, 3}, {5, 0}}, 1},
	}
	for _, c := range cases {
		if got := CountSegments(c.n, c.links); got != c.want {
			t.Errorf("CountSegments(%d, %v) = %d, want %d", c.n, c.links, got, c.want)
		}
	}
}

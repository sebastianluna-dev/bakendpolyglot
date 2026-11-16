package p139

import "testing"

func TestCheapestRoute(t *testing.T) {
	legs := [][]int{{0, 1, 100}, {1, 2, 100}, {2, 3, 100}, {0, 2, 500}, {1, 3, 600}}
	triangle := [][]int{{0, 1, 1}, {1, 2, 1}, {0, 2, 5}}
	cases := []struct {
		n                 int
		legs              [][]int
		source, target, k int
		want              int
	}{
		{4, legs, 0, 3, 1, 600},
		{4, legs, 0, 3, 2, 300},
		{4, legs, 0, 3, 0, -1},
		{4, legs, 0, 3, 10, 300},
		{3, triangle, 0, 2, 0, 5},
		{3, triangle, 0, 2, 1, 2},
		{2, [][]int{{0, 1, 7}}, 1, 1, 0, 0},
		{3, [][]int{{0, 1, 4}}, 0, 2, 5, -1},
		{2, [][]int{{1, 0, 3}}, 0, 1, 3, -1},
	}
	for _, c := range cases {
		if got := CheapestRoute(c.n, c.legs, c.source, c.target, c.k); got != c.want {
			t.Errorf("CheapestRoute(%d, %v, %d, %d, %d) = %d, want %d", c.n, c.legs, c.source, c.target, c.k, got, c.want)
		}
	}
}

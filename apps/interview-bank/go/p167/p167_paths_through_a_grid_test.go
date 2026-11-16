package p167

import "testing"

func TestGridPaths(t *testing.T) {
	cases := []struct {
		rows, cols int
		want       int
	}{
		{3, 4, 10},
		{2, 2, 2},
		{1, 1, 1},
		{1, 5, 1},
		{5, 1, 1},
		{3, 7, 28},
		{10, 10, 48620},
		{17, 17, 601080390},
	}
	for _, c := range cases {
		if got := GridPaths(c.rows, c.cols); got != c.want {
			t.Errorf("GridPaths(%d, %d) = %d, want %d", c.rows, c.cols, got, c.want)
		}
	}
}

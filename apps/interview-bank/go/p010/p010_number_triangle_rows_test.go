package p010

import (
	"slices"
	"testing"
)

func TestTriangleRows(t *testing.T) {
	cases := []struct {
		n    int
		want [][]int
	}{
		{5, [][]int{{1}, {1, 1}, {1, 2, 1}, {1, 3, 3, 1}, {1, 4, 6, 4, 1}}},
		{2, [][]int{{1}, {1, 1}}},
		{1, [][]int{{1}}},
		{0, [][]int{}},
	}
	for _, c := range cases {
		if got := TriangleRows(c.n); !slices.EqualFunc(got, c.want, slices.Equal[[]int]) {
			t.Errorf("TriangleRows(%d) = %v, want %v", c.n, got, c.want)
		}
	}
	if got := TriangleRows(30)[29][14]; got != 77558760 {
		t.Errorf("TriangleRows(30)[29][14] = %d, want 77558760", got)
	}
}

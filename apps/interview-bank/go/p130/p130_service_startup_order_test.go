package p130

import (
	"slices"
	"testing"
)

func TestStartupOrder(t *testing.T) {
	cases := []struct {
		n            int
		dependencies [][]int
		want         []int
	}{
		{5, [][]int{{0, 3}, {2, 4}, {1, 0}}, []int{3, 0, 1, 4, 2}},
		{3, [][]int{{0, 1}, {1, 2}, {2, 0}}, []int{}},
		{3, [][]int{}, []int{0, 1, 2}},
		{1, [][]int{}, []int{0}},
		{2, [][]int{{1, 1}}, []int{}},
		{3, [][]int{{2, 0}, {2, 0}, {1, 2}}, []int{0, 2, 1}},
		{4, [][]int{{1, 0}, {2, 3}, {3, 2}}, []int{}},
	}
	for _, c := range cases {
		if got := StartupOrder(c.n, c.dependencies); !slices.Equal(got, c.want) {
			t.Errorf("StartupOrder(%d, %v) = %v, want %v", c.n, c.dependencies, got, c.want)
		}
	}
}

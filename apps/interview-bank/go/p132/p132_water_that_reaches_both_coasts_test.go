package p132

import (
	"slices"
	"testing"
)

func TestBothCoasts(t *testing.T) {
	cases := []struct {
		heights [][]int
		want    [][2]int
	}{
		{[][]int{{1, 2, 2, 3}, {3, 2, 3, 4}, {2, 4, 5, 3}, {6, 7, 1, 4}}, [][2]int{{0, 3}, {1, 3}, {2, 2}, {3, 0}, {3, 1}}},
		{[][]int{{5, 5, 5}, {5, 1, 5}, {5, 5, 5}}, [][2]int{{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 2}, {2, 0}, {2, 1}, {2, 2}}},
		{[][]int{{4, 3, 2}, {5, 1, 3}, {6, 2, 1}}, [][2]int{{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 2}, {2, 0}}},
		{[][]int{{1, 2, 3}}, [][2]int{{0, 0}, {0, 1}, {0, 2}}},
		{[][]int{{7}}, [][2]int{{0, 0}}},
	}
	for _, c := range cases {
		if got := BothCoasts(c.heights); !slices.Equal(got, c.want) {
			t.Errorf("BothCoasts(%v) = %v, want %v", c.heights, got, c.want)
		}
	}
}

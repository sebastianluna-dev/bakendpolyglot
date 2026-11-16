package p185

import (
	"slices"
	"testing"
)

func TestMergeIntervals(t *testing.T) {
	cases := []struct {
		intervals [][]int
		want      [][]int
	}{
		{[][]int{{9, 12}, {1, 3}, {2, 6}, {8, 9}}, [][]int{{1, 6}, {8, 12}}},
		{[][]int{{1, 10}, {2, 3}, {4, 5}}, [][]int{{1, 10}}},
		{[][]int{}, [][]int{}},
		{[][]int{{5, 5}}, [][]int{{5, 5}}},
		{[][]int{{3, 4}, {1, 2}}, [][]int{{1, 2}, {3, 4}}},
		{[][]int{{4, 6}, {1, 4}, {6, 6}}, [][]int{{1, 6}}},
	}
	for _, c := range cases {
		if got := MergeIntervals(c.intervals); !slices.EqualFunc(got, c.want, slices.Equal[[]int]) {
			t.Errorf("MergeIntervals(%v) = %v, want %v", c.intervals, got, c.want)
		}
	}
}

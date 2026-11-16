package p186

import (
	"slices"
	"testing"
)

func TestInsertInterval(t *testing.T) {
	cases := []struct {
		intervals   [][]int
		newInterval []int
		want        [][]int
	}{
		{[][]int{{1, 3}, {6, 8}, {10, 12}, {15, 18}}, []int{7, 10}, [][]int{{1, 3}, {6, 12}, {15, 18}}},
		{[][]int{{2, 4}, {9, 11}}, []int{5, 7}, [][]int{{2, 4}, {5, 7}, {9, 11}}},
		{[][]int{}, []int{4, 6}, [][]int{{4, 6}}},
		{[][]int{{5, 6}}, []int{1, 2}, [][]int{{1, 2}, {5, 6}}},
		{[][]int{{1, 2}}, []int{7, 9}, [][]int{{1, 2}, {7, 9}}},
		{[][]int{{3, 4}, {6, 7}}, []int{1, 10}, [][]int{{1, 10}}},
		{[][]int{{1, 3}}, []int{3, 5}, [][]int{{1, 5}}},
		{[][]int{{5, 8}}, []int{2, 5}, [][]int{{2, 8}}},
	}
	for _, c := range cases {
		if got := InsertInterval(c.intervals, c.newInterval); !slices.EqualFunc(got, c.want, slices.Equal[[]int]) {
			t.Errorf("InsertInterval(%v, %v) = %v, want %v", c.intervals, c.newInterval, got, c.want)
		}
	}
}

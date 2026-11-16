package p099

import "testing"

func TestSearchSortedMatrix(t *testing.T) {
	branch := [][]int{
		{1, 4, 7, 11},
		{15, 18, 21, 26},
		{30, 34, 40, 52},
	}
	column := [][]int{{1}, {3}, {5}}
	cases := []struct {
		shelves [][]int
		target  int
		want    bool
	}{
		{branch, 21, true},
		{branch, 13, false},
		{[][]int{}, 1, false},
		{[][]int{{}}, 1, false},
		{[][]int{{5}}, 5, true},
		{[][]int{{5}}, 6, false},
		{branch, 60, false},
		{branch, 0, false},
		{branch, 1, true},
		{branch, 52, true},
		{branch, 26, true},
		{branch, 30, true},
		{column, 3, true},
		{column, 4, false},
	}
	for _, c := range cases {
		if got := SearchSortedMatrix(c.shelves, c.target); got != c.want {
			t.Errorf("SearchSortedMatrix(%v, %d) = %v, want %v", c.shelves, c.target, got, c.want)
		}
	}
}

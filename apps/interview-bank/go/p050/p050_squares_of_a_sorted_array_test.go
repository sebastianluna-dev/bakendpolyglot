package p050

import (
	"slices"
	"testing"
)

func TestSortedSquares(t *testing.T) {
	cases := []struct {
		nums []int
		want []int
	}{
		{[]int{-5, -2, 0, 3, 4}, []int{0, 4, 9, 16, 25}},
		{[]int{-3, -3, -1}, []int{1, 9, 9}},
		{[]int{}, []int{}},
		{[]int{2, 5}, []int{4, 25}},
		{[]int{-7}, []int{49}},
		{[]int{-4, -1, 1, 4}, []int{1, 1, 16, 16}},
	}
	for _, c := range cases {
		if got := SortedSquares(c.nums); !slices.Equal(got, c.want) {
			t.Errorf("SortedSquares(%v) = %v, want %v", c.nums, got, c.want)
		}
	}
}

package p176

import (
	"slices"
	"testing"
)

func TestLargestProduct(t *testing.T) {
	cases := []struct {
		nums []int
		want int
	}{
		{[]int{2, -5, -2, 4, 0, 3}, 80},
		{[]int{-3, 0, -2}, 0},
		{[]int{-2}, -2},
		{[]int{-2, 3, -4}, 24},
		{[]int{0, 0}, 0},
		{[]int{-1, -1, -1}, 1},
		{[]int{3, -1, 4}, 4},
		{[]int{-4, 0, -5, 0, -6, 2}, 2},
		{slices.Repeat([]int{10}, 9), 1000000000},
	}
	for _, c := range cases {
		if got := LargestProduct(c.nums); got != c.want {
			t.Errorf("LargestProduct(%v) = %d, want %d", c.nums, got, c.want)
		}
	}
}

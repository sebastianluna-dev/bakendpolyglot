package p016

import (
	"slices"
	"testing"
)

func TestProductOfOthers(t *testing.T) {
	cases := []struct {
		nums []int
		want []int
	}{
		{[]int{2, 3, 4, 5}, []int{60, 40, 30, 24}},
		{[]int{4, 0, 5}, []int{0, 20, 0}},
		{[]int{3, 0, 2, 0}, []int{0, 0, 0, 0}},
		{[]int{-1, 2, -3}, []int{-6, 3, -2}},
		{[]int{7}, []int{1}},
		{[]int{}, []int{}},
	}
	for _, c := range cases {
		if got := ProductOfOthers(c.nums); !slices.Equal(got, c.want) {
			t.Errorf("ProductOfOthers(%v) = %v, want %v", c.nums, got, c.want)
		}
	}
}

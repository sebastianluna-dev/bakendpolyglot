package p047

import (
	"slices"
	"testing"
)

func TestMergeInPlace(t *testing.T) {
	cases := []struct {
		a    []int
		m    int
		b    []int
		want []int
	}{
		{[]int{1, 4, 7, 0, 0}, 3, []int{2, 8}, []int{1, 2, 4, 7, 8}},
		{[]int{0, 0}, 0, []int{3, 5}, []int{3, 5}},
		{[]int{2, 3}, 2, []int{}, []int{2, 3}},
		{[]int{1, 2, 2, 0, 0, 0}, 3, []int{2, 2, 3}, []int{1, 2, 2, 2, 2, 3}},
		{[]int{-3, 5, 0}, 2, []int{-4}, []int{-4, -3, 5}},
		{[]int{4, 5, 0}, 2, []int{1}, []int{1, 4, 5}},
	}
	for _, c := range cases {
		MergeInPlace(c.a, c.m, c.b)
		if !slices.Equal(c.a, c.want) {
			t.Errorf("MergeInPlace left %v, want %v", c.a, c.want)
		}
	}
}

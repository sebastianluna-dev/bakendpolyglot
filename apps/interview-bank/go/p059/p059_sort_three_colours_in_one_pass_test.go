package p059

import (
	"slices"
	"testing"
)

func TestSortColours(t *testing.T) {
	cases := []struct {
		stickers []int
		want     []int
	}{
		{[]int{2, 0, 1, 2, 1, 0}, []int{0, 0, 1, 1, 2, 2}},
		{[]int{1, 1, 1}, []int{1, 1, 1}},
		{[]int{}, []int{}},
		{[]int{2, 0}, []int{0, 2}},
		{[]int{2, 2, 0, 0}, []int{0, 0, 2, 2}},
		{[]int{0, 2, 1}, []int{0, 1, 2}},
		{[]int{1, 2, 0}, []int{0, 1, 2}},
		{[]int{1, 2, 0, 2, 0, 1, 0}, []int{0, 0, 0, 1, 1, 2, 2}},
	}
	for _, c := range cases {
		got := slices.Clone(c.stickers)
		SortColours(got)
		if !slices.Equal(got, c.want) {
			t.Errorf("SortColours(%v) left %v, want %v", c.stickers, got, c.want)
		}
	}
}

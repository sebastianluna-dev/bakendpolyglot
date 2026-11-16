package p013

import (
	"slices"
	"testing"
)

func TestCollapseRanges(t *testing.T) {
	cases := []struct {
		ids  []int
		want []string
	}{
		{[]int{1, 2, 3, 7, 9, 10}, []string{"1-3", "7", "9-10"}},
		{[]int{0, 2, 4}, []string{"0", "2", "4"}},
		{[]int{}, []string{}},
		{[]int{5}, []string{"5"}},
		{[]int{40, 41, 42, 43}, []string{"40-43"}},
	}
	for _, c := range cases {
		if got := CollapseRanges(c.ids); !slices.Equal(got, c.want) {
			t.Errorf("CollapseRanges(%v) = %v, want %v", c.ids, got, c.want)
		}
	}
}

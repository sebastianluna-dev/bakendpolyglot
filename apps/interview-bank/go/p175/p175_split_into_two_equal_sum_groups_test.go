package p175

import (
	"slices"
	"testing"
)

func TestCanSplitEvenly(t *testing.T) {
	cases := []struct {
		weights []int
		want    bool
	}{
		{[]int{3, 1, 4, 2, 2}, true},
		{[]int{2, 3, 7}, false},
		{[]int{1}, false},
		{[]int{5, 5}, true},
		{[]int{1, 5}, false},
		{[]int{1, 2, 4}, false},
		{[]int{1, 2, 3, 4, 5, 6, 7}, true},
		{[]int{6, 6, 6, 6, 6, 4}, false},
		{slices.Repeat([]int{100}, 200), true},
	}
	for _, c := range cases {
		if got := CanSplitEvenly(c.weights); got != c.want {
			t.Errorf("CanSplitEvenly(%v) = %t, want %t", c.weights, got, c.want)
		}
	}
}

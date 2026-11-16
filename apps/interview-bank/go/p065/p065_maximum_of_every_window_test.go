package p065

import (
	"slices"
	"testing"
)

func TestWindowMaxima(t *testing.T) {
	cases := []struct {
		readings []int
		k        int
		want     []int
	}{
		{[]int{3, 1, -2, 5, 4, 2, 6}, 3, []int{3, 5, 5, 5, 6}},
		{[]int{2, 7, 1}, 3, []int{7}},
		{[]int{4, -1, 4}, 1, []int{4, -1, 4}},
		{[]int{}, 2, []int{}},
		{[]int{1, 2}, 3, []int{}},
		{[]int{5, 5, 1, 5}, 2, []int{5, 5, 5}},
		{[]int{9, 7, 5, 3}, 2, []int{9, 7, 5}},
		{[]int{9, 1, 1, 1}, 2, []int{9, 1, 1}},
	}
	for _, c := range cases {
		if got := WindowMaxima(c.readings, c.k); !slices.Equal(got, c.want) {
			t.Errorf("WindowMaxima(%v, %d) = %v, want %v", c.readings, c.k, got, c.want)
		}
	}
}

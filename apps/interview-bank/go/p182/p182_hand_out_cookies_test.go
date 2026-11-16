package p182

import "testing"

func TestContentChildren(t *testing.T) {
	cases := []struct {
		greed, sizes []int
		want         int
	}{
		{[]int{5, 2, 8, 2}, []int{3, 1, 6, 9}, 3},
		{[]int{1, 3}, []int{3, 1}, 2},
		{[]int{5, 2, 8, 2}, []int{3, 1, 6}, 2},
		{[]int{4, 4}, []int{}, 0},
		{[]int{}, []int{1, 2}, 0},
		{[]int{10, 20}, []int{1, 2, 3}, 0},
		{[]int{2, 2, 2}, []int{2, 2}, 2},
		{[]int{1, 2, 3}, []int{3}, 1},
	}
	for _, c := range cases {
		if got := ContentChildren(c.greed, c.sizes); got != c.want {
			t.Errorf("ContentChildren(%v, %v) = %d, want %d", c.greed, c.sizes, got, c.want)
		}
	}
}

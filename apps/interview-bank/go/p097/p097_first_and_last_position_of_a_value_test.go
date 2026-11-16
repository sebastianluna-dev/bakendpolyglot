package p097

import (
	"slices"
	"testing"
)

func TestFirstAndLast(t *testing.T) {
	due := []int{3, 5, 5, 5, 8, 9}
	none := []int{-1, -1}
	cases := []struct {
		days []int
		day  int
		want []int
	}{
		{due, 5, []int{1, 3}},
		{[]int{3, 5, 8}, 6, none},
		{[]int{}, 1, none},
		{[]int{7, 7, 7, 7}, 7, []int{0, 3}},
		{[]int{1}, 1, []int{0, 0}},
		{[]int{1, 2, 2}, 2, []int{1, 2}},
		{due, 1, none},
		{due, 10, none},
		{due, 3, []int{0, 0}},
		{due, 9, []int{5, 5}},
	}
	for _, c := range cases {
		if got := FirstAndLast(c.days, c.day); !slices.Equal(got, c.want) {
			t.Errorf("FirstAndLast(%v, %d) = %v, want %v", c.days, c.day, got, c.want)
		}
	}
}

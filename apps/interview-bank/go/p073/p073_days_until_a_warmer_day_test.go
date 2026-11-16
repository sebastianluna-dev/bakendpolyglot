package p073

import (
	"slices"
	"testing"
)

func TestDaysUntilWarmer(t *testing.T) {
	cases := []struct {
		temps []int
		want  []int
	}{
		{[]int{18, 16, 17, 21, 20, 20, 23}, []int{3, 1, 1, 3, 2, 1, 0}},
		{[]int{30, 25, 20}, []int{0, 0, 0}},
		{[]int{}, []int{}},
		{[]int{12}, []int{0}},
		{[]int{5, 5, 5}, []int{0, 0, 0}},
		{[]int{-3, -1, 0, 2}, []int{1, 1, 1, 0}},
	}
	for _, c := range cases {
		if got := DaysUntilWarmer(c.temps); !slices.Equal(got, c.want) {
			t.Errorf("DaysUntilWarmer(%v) = %v, want %v", c.temps, got, c.want)
		}
	}
}

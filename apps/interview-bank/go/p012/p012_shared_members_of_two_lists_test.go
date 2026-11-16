package p012

import (
	"slices"
	"testing"
)

func TestSharedMembers(t *testing.T) {
	cases := []struct {
		first, second []int
		want          []int
	}{
		{[]int{104, 211, 104, 350, 87}, []int{87, 104, 999, 104}, []int{104, 87}},
		{[]int{1, 2}, []int{3, 4}, []int{}},
		{[]int{}, []int{1}, []int{}},
		{[]int{5, 5, 5}, []int{5}, []int{5}},
		{[]int{3, 1, 2}, []int{2, 3}, []int{3, 2}},
	}
	for _, c := range cases {
		if got := SharedMembers(c.first, c.second); !slices.Equal(got, c.want) {
			t.Errorf("SharedMembers(%v, %v) = %v, want %v", c.first, c.second, got, c.want)
		}
	}
}

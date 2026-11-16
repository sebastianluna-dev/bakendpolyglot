package p024

import "testing"

func TestSmallestMissingPositive(t *testing.T) {
	cases := []struct {
		nums []int
		want int
	}{
		{[]int{3, 1, -2, 2, 7}, 4},
		{[]int{5, 2, 2, 9}, 1},
		{[]int{}, 1},
		{[]int{1, 2, 3}, 4},
		{[]int{0, -1, -5}, 1},
		{[]int{1, 1}, 2},
		{[]int{2, 3, 4, 1}, 5},
		{[]int{2147483647, 1}, 2},
	}
	for _, c := range cases {
		input := append([]int{}, c.nums...)
		if got := SmallestMissingPositive(c.nums); got != c.want {
			t.Errorf("SmallestMissingPositive(%v) = %d, want %d", input, got, c.want)
		}
	}
}

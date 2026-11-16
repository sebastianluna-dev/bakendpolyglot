package p171

import "testing"

func TestLongestRising(t *testing.T) {
	cases := []struct {
		nums []int
		want int
	}{
		{[]int{3, 1, 8, 2, 5, 9, 4}, 4},
		{[]int{7, 7, 7}, 1},
		{[]int{}, 0},
		{[]int{5}, 1},
		{[]int{9, 8, 7}, 1},
		{[]int{1, 2, 3, 4}, 4},
		{[]int{2, 2, 3, 3, 4}, 3},
		{[]int{6, 2, 9, 3, 7, 4, 10, 5, 8}, 5},
		{[]int{10, -3, 4, -1, 6, 2, 8}, 4},
	}
	for _, c := range cases {
		if got := LongestRising(c.nums); got != c.want {
			t.Errorf("LongestRising(%v) = %d, want %d", c.nums, got, c.want)
		}
	}
}

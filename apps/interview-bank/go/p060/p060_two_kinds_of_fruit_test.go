package p060

import "testing"

func TestLongestTwoKinds(t *testing.T) {
	cases := []struct {
		crates []int
		want   int
	}{
		{[]int{3, 3, 7, 3, 9, 9, 7}, 4},
		{[]int{5, 5, 5}, 3},
		{[]int{}, 0},
		{[]int{1, 2, 3, 4}, 2},
		{[]int{1, 2, 1, 3, 3, 3, 1}, 5},
		{[]int{4}, 1},
	}
	for _, c := range cases {
		if got := LongestTwoKinds(c.crates); got != c.want {
			t.Errorf("LongestTwoKinds(%v) = %d, want %d", c.crates, got, c.want)
		}
	}
}

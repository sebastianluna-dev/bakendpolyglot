package p190

import "testing"

func TestMinCandy(t *testing.T) {
	cases := []struct {
		scores []int
		want   int
	}{
		{[]int{1, 3, 2, 2, 1}, 7},
		{[]int{5, 4, 3, 4}, 8},
		{[]int{}, 0},
		{[]int{7}, 1},
		{[]int{2, 2, 2}, 3},
		{[]int{1, 2, 3, 4}, 10},
		{[]int{1, 2, 3, 4, 1}, 11},
		{[]int{4, 3, 2, 1}, 10},
	}
	for _, c := range cases {
		if got := MinCandy(c.scores); got != c.want {
			t.Errorf("MinCandy(%v) = %d, want %d", c.scores, got, c.want)
		}
	}
}

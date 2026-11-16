package p079

import "testing"

func TestBiggestBanner(t *testing.T) {
	cases := []struct {
		heights []int
		want    int
	}{
		{[]int{3, 1, 4, 5, 4, 2}, 12},
		{[]int{2, 4, 2}, 6},
		{[]int{}, 0},
		{[]int{7}, 7},
		{[]int{1, 2, 3, 4, 5}, 9},
		{[]int{5, 4, 3, 2, 1}, 9},
		{[]int{2, 1, 2}, 3},
		{[]int{0, 0}, 0},
		{[]int{4, 0, 4}, 4},
	}
	for _, c := range cases {
		if got := BiggestBanner(c.heights); got != c.want {
			t.Errorf("BiggestBanner(%v) = %d, want %d", c.heights, got, c.want)
		}
	}
}

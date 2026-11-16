package p095

import "testing"

func TestSearchRotated(t *testing.T) {
	board := []int{900, 1020, 1200, 60, 300, 480}
	cases := []struct {
		times  []int
		target int
		want   int
	}{
		{board, 300, 4},
		{board, 600, -1},
		{board, 1020, 1},
		{board, 900, 0},
		{board, 480, 5},
		{board, 60, 3},
		{board, 1300, -1},
		{board, 10, -1},
		{[]int{1, 2, 3}, 3, 2},
		{[]int{5}, 5, 0},
		{[]int{5}, 1, -1},
		{[]int{}, 1, -1},
		{[]int{2, 1}, 1, 1},
		{[]int{2, 1}, 2, 0},
	}
	for _, c := range cases {
		if got := SearchRotated(c.times, c.target); got != c.want {
			t.Errorf("SearchRotated(%v, %d) = %d, want %d", c.times, c.target, got, c.want)
		}
	}
}

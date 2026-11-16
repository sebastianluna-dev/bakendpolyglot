package p061

import "testing"

func TestLongestOnes(t *testing.T) {
	cases := []struct {
		seats []int
		k     int
		want  int
	}{
		{[]int{1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1}, 2, 6},
		{[]int{1, 0, 1, 1, 1, 0}, 0, 3},
		{[]int{}, 3, 0},
		{[]int{0, 0, 0}, 0, 0},
		{[]int{0, 0, 0}, 5, 3},
		{[]int{1, 0, 0, 1}, 1, 2},
		{[]int{1, 1, 1}, 1, 3},
	}
	for _, c := range cases {
		if got := LongestOnes(c.seats, c.k); got != c.want {
			t.Errorf("LongestOnes(%v, %d) = %d, want %d", c.seats, c.k, got, c.want)
		}
	}
}

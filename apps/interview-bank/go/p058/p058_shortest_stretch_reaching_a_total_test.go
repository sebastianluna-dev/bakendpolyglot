package p058

import "testing"

func TestShortestReaching(t *testing.T) {
	cases := []struct {
		weights []int
		target  int
		want    int
	}{
		{[]int{3, 1, 4, 1, 5, 9, 2}, 15, 3},
		{[]int{2, 2, 2}, 7, 0},
		{[]int{}, 5, 0},
		{[]int{8}, 8, 1},
		{[]int{1, 1, 1, 1}, 4, 4},
		{[]int{1, 6, 1, 1, 7}, 7, 1},
	}
	for _, c := range cases {
		if got := ShortestReaching(c.weights, c.target); got != c.want {
			t.Errorf("ShortestReaching(%v, %d) = %d, want %d", c.weights, c.target, got, c.want)
		}
	}
}

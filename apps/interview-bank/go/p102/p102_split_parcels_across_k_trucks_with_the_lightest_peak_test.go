package p102

import "testing"

func TestLightestPeak(t *testing.T) {
	cases := []struct {
		weights []int
		k       int
		want    int
	}{
		{[]int{4, 9, 3, 6, 8, 2}, 3, 13},
		{[]int{3, 1, 12, 2}, 3, 12},
		{[]int{4, 9, 3}, 1, 16},
		{[]int{2, 8, 5}, 3, 8},
		{[]int{7}, 1, 7},
		{[]int{1, 1, 1, 1, 1, 1}, 4, 2},
	}
	for _, c := range cases {
		if got := LightestPeak(c.weights, c.k); got != c.want {
			t.Errorf("LightestPeak(%v, %d) = %d, want %d", c.weights, c.k, got, c.want)
		}
	}
}

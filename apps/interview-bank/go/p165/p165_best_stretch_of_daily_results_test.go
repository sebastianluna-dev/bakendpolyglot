package p165

import "testing"

func TestBestStretch(t *testing.T) {
	cases := []struct {
		results []int
		want    int
	}{
		{[]int{-2, 4, -1, 3, -7, 2}, 6},
		{[]int{-8, -3, -5}, -3},
		{[]int{5}, 5},
		{[]int{1, 2, 3}, 6},
		{[]int{-1, 0, -2}, 0},
		{[]int{2, -1, 2, -9, 4}, 4},
		{[]int{3, -2, 5, -1}, 6},
	}
	for _, c := range cases {
		if got := BestStretch(c.results); got != c.want {
			t.Errorf("BestStretch(%v) = %d, want %d", c.results, got, c.want)
		}
	}
}

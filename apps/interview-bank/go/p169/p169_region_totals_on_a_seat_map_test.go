package p169

import "testing"

func TestRegionSums(t *testing.T) {
	seats := NewRegionSums([][]int{
		{3, 0, 1, 4},
		{5, 6, 3, 2},
		{1, 2, 0, 1},
		{4, 1, 0, 5},
	})
	cases := []struct {
		r1, c1, r2, c2 int
		want           int
	}{
		{1, 1, 2, 2, 11},
		{0, 0, 3, 3, 38},
		{2, 3, 2, 3, 1},
		{0, 1, 3, 2, 13},
		{1, 0, 1, 3, 16},
		{2, 2, 3, 3, 6},
	}
	for _, c := range cases {
		if got := seats.Sum(c.r1, c.c1, c.r2, c.c2); got != c.want {
			t.Errorf("Sum(%d, %d, %d, %d) = %d, want %d", c.r1, c.c1, c.r2, c.c2, got, c.want)
		}
	}
	if got := NewRegionSums([][]int{{7}}).Sum(0, 0, 0, 0); got != 7 {
		t.Errorf("one-seat map: Sum(0, 0, 0, 0) = %d, want 7", got)
	}
}

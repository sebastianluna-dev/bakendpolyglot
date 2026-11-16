package p163

import "testing"

func TestWaysToClimb(t *testing.T) {
	cases := []struct {
		n    int
		want int64
	}{
		{4, 5},
		{1, 1},
		{0, 1},
		{2, 2},
		{10, 89},
		{46, 2971215073},
		{70, 308061521170129},
	}
	for _, c := range cases {
		if got := WaysToClimb(c.n); got != c.want {
			t.Errorf("WaysToClimb(%d) = %d, want %d", c.n, got, c.want)
		}
	}
}

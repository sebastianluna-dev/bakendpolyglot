package p151

import "testing"

func TestPowerMod(t *testing.T) {
	cases := []struct {
		x, n, m int
		want    int
	}{
		{3, 13, 100, 23},
		{7, 0, 13, 1},
		{2, 10, 1000, 24},
		{5, 3, 1, 0},
		{0, 0, 7, 1},
		{0, 5, 7, 0},
		{999999999, 2, 1000000007, 64},
		{10, 9, 1000000007, 1000000000},
		{2, 1000000006, 1000000007, 1},
	}
	for _, c := range cases {
		if got := PowerMod(c.x, c.n, c.m); got != c.want {
			t.Errorf("PowerMod(%d, %d, %d) = %d, want %d", c.x, c.n, c.m, got, c.want)
		}
	}
}

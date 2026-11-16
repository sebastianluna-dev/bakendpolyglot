package p093

import "testing"

func TestFirstFailingBuild(t *testing.T) {
	cases := []struct {
		n, first, want, maxCalls int
	}{
		{5, 4, 4, 0},
		{3, 4, -1, 0},
		{1, 1, 1, 0},
		{1, 2, -1, 0},
		{5, 1, 1, 0},
		{1000, 777, 777, 10},
		{2_000_000_000, 1_999_999_999, 1_999_999_999, 31},
	}
	for _, c := range cases {
		calls := 0
		isBad := func(build int) bool {
			calls++
			return build >= c.first
		}
		if got := FirstFailingBuild(c.n, isBad); got != c.want {
			t.Errorf("FirstFailingBuild(%d, failing from %d) = %d, want %d", c.n, c.first, got, c.want)
		}
		if c.maxCalls > 0 && calls > c.maxCalls {
			t.Errorf("FirstFailingBuild(%d, ...) called isBad %d times, want at most %d", c.n, calls, c.maxCalls)
		}
	}
}

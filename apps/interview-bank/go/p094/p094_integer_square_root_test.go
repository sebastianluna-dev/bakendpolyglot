package p094

import (
	"math"
	"testing"
)

func TestIntegerSqrt(t *testing.T) {
	cases := []struct{ x, want int }{
		{17, 4},
		{36, 6},
		{0, 0},
		{1, 1},
		{2, 1},
		{4, 2},
		{2_147_395_599, 46_339},
		{2_147_395_600, 46_340},
		{math.MaxInt32, 46_340},
	}
	for _, c := range cases {
		if got := IntegerSqrt(c.x); got != c.want {
			t.Errorf("IntegerSqrt(%d) = %d, want %d", c.x, got, c.want)
		}
	}
}

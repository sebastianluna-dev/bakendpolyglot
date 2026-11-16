package p153

import (
	"slices"
	"testing"
)

func TestNoAdjacentOnes(t *testing.T) {
	cases := []struct {
		n    int
		want []string
	}{
		{3, []string{"000", "001", "010", "100", "101"}},
		{1, []string{"0", "1"}},
		{0, []string{""}},
		{2, []string{"00", "01", "10"}},
		{4, []string{"0000", "0001", "0010", "0100", "0101", "1000", "1001", "1010"}},
	}
	for _, c := range cases {
		if got := NoAdjacentOnes(c.n); !slices.Equal(got, c.want) {
			t.Errorf("NoAdjacentOnes(%d) = %v, want %v", c.n, got, c.want)
		}
	}
	if got := len(NoAdjacentOnes(10)); got != 144 {
		t.Errorf("len(NoAdjacentOnes(10)) = %d, want 144", got)
	}
}

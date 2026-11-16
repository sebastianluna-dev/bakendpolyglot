package p071

import (
	"slices"
	"testing"
)

func TestEliminationOrder(t *testing.T) {
	cases := []struct {
		n, k int
		want []int
	}{
		{5, 2, []int{2, 4, 1, 5, 3}},
		{4, 3, []int{3, 2, 4, 1}},
		{1, 7, []int{1}},
		{4, 1, []int{1, 2, 3, 4}},
		{3, 5, []int{2, 3, 1}},
		{6, 6, []int{6, 1, 3, 2, 5, 4}},
	}
	for _, c := range cases {
		if got := EliminationOrder(c.n, c.k); !slices.Equal(got, c.want) {
			t.Errorf("EliminationOrder(%d, %d) = %v, want %v", c.n, c.k, got, c.want)
		}
	}
}

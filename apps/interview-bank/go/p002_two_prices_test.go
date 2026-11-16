package bank

import (
	"slices"
	"testing"
)

func TestTwoPrices(t *testing.T) {
	cases := []struct {
		prices []int
		budget int
		want   []int
	}{
		{[]int{12, 7, 3, 9, 5}, 12, []int{2, 3}},
		{[]int{4, 4, 1}, 8, []int{0, 1}},
		{[]int{1, 2}, 10, []int{}},
	}
	for _, c := range cases {
		if got := TwoPrices(c.prices, c.budget); !slices.Equal(got, c.want) {
			t.Errorf("TwoPrices(%v, %d) = %v, want %v", c.prices, c.budget, got, c.want)
		}
	}
}

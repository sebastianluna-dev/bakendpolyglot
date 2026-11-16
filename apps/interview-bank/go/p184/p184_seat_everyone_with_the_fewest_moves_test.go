package p184

import "testing"

func TestFewestMoves(t *testing.T) {
	cases := []struct {
		seats, guests []int
		want          int
	}{
		{[]int{4, 1, 7}, []int{2, 8, 3}, 3},
		{[]int{2, 6, 9, 10}, []int{5, 5, 1, 12}, 8},
		{[]int{}, []int{}, 0},
		{[]int{3, 5}, []int{5, 3}, 0},
		{[]int{10}, []int{1}, 9},
	}
	for _, c := range cases {
		if got := FewestMoves(c.seats, c.guests); got != c.want {
			t.Errorf("FewestMoves(%v, %v) = %d, want %d", c.seats, c.guests, got, c.want)
		}
	}
}

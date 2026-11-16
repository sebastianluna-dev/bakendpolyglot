package p006

import "testing"

func TestUnpairedSeat(t *testing.T) {
	cases := []struct {
		log  []int
		want int
	}{
		{[]int{14, 3, 14, 8, 3}, 8},
		{[]int{21}, 21},
		{[]int{4, 4, 9}, 9},
		{[]int{7, 2, 2}, 7},
		{[]int{1000000000, 5, 5}, 1000000000},
	}
	for _, c := range cases {
		if got := UnpairedSeat(c.log); got != c.want {
			t.Errorf("UnpairedSeat(%v) = %d, want %d", c.log, got, c.want)
		}
	}
}

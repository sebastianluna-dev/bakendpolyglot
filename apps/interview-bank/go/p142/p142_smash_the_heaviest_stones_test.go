package p142

import "testing"

func TestLastStone(t *testing.T) {
	cases := []struct {
		stones []int
		want   int
	}{
		{[]int{3, 9, 4, 4, 1}, 1},
		{[]int{10, 4, 4, 2}, 0},
		{[]int{6, 6}, 0},
		{[]int{7}, 7},
		{[]int{1, 1, 1}, 1},
		{[]int{2, 2, 3}, 1},
		{[]int{5, 1, 1, 1, 1, 1}, 0},
	}
	for _, c := range cases {
		if got := LastStone(c.stones); got != c.want {
			t.Errorf("LastStone(%v) = %d, want %d", c.stones, got, c.want)
		}
	}
}

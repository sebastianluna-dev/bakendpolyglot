package p148

import "testing"

func TestFurthestPlatform(t *testing.T) {
	cases := []struct {
		heights         []int
		bricks, ladders int
		want            int
	}{
		{[]int{3, 8, 6, 10, 11, 15, 7}, 4, 1, 3},
		{[]int{10, 12, 13, 25, 26}, 4, 1, 4},
		{[]int{7}, 0, 0, 0},
		{[]int{9, 7, 7, 3}, 0, 0, 3},
		{[]int{1, 2}, 0, 0, 0},
		{[]int{1, 2, 3, 4}, 0, 3, 3},
		{[]int{1, 6, 2, 9, 4, 5}, 13, 0, 5},
		{[]int{1, 6, 2, 9, 4, 5}, 12, 0, 4},
	}
	for _, c := range cases {
		if got := FurthestPlatform(c.heights, c.bricks, c.ladders); got != c.want {
			t.Errorf("FurthestPlatform(%v, %d, %d) = %d, want %d", c.heights, c.bricks, c.ladders, got, c.want)
		}
	}
}

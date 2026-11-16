package p188

import "testing"

func TestRoomsNeeded(t *testing.T) {
	cases := []struct {
		meetings [][]int
		want     int
	}{
		{[][]int{{9, 12}, {10, 11}, {11, 13}, {13, 14}}, 2},
		{[][]int{{8, 10}, {10, 12}, {12, 14}}, 1},
		{[][]int{}, 0},
		{[][]int{{1, 5}, {2, 6}, {3, 7}}, 3},
		{[][]int{{2, 3}, {2, 3}}, 2},
		{[][]int{{5, 10}, {1, 4}, {4, 6}}, 2},
	}
	for _, c := range cases {
		if got := RoomsNeeded(c.meetings); got != c.want {
			t.Errorf("RoomsNeeded(%v) = %d, want %d", c.meetings, got, c.want)
		}
	}
}

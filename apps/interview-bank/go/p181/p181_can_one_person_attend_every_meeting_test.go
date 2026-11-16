package p181

import "testing"

func TestCanAttendAll(t *testing.T) {
	cases := []struct {
		meetings [][]int
		want     bool
	}{
		{[][]int{{540, 600}, {600, 660}, {480, 540}}, true},
		{[][]int{{540, 630}, {600, 660}}, false},
		{[][]int{}, true},
		{[][]int{{100, 200}}, true},
		{[][]int{{700, 800}, {100, 200}, {150, 160}}, false},
		{[][]int{{5, 10}, {5, 6}}, false},
		{[][]int{{0, 1}, {2, 3}, {1, 2}}, true},
	}
	for _, c := range cases {
		if got := CanAttendAll(c.meetings); got != c.want {
			t.Errorf("CanAttendAll(%v) = %t, want %t", c.meetings, got, c.want)
		}
	}
}

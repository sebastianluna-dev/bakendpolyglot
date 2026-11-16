package p096

import "testing"

func TestRotatedMinimum(t *testing.T) {
	cases := []struct {
		times []int
		want  int
	}{
		{[]int{900, 1020, 1200, 60, 300, 480}, 60},
		{[]int{60, 300, 480}, 60},
		{[]int{7}, 7},
		{[]int{2, 1}, 1},
		{[]int{3, 4, 5, 1, 2}, 1},
		{[]int{2, 3, 4, 5, 1}, 1},
		{[]int{5, 1, 2, 3, 4}, 1},
	}
	for _, c := range cases {
		if got := RotatedMinimum(c.times); got != c.want {
			t.Errorf("RotatedMinimum(%v) = %d, want %d", c.times, got, c.want)
		}
	}
}

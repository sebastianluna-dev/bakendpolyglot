package p189

import "testing"

func TestCanReachLast(t *testing.T) {
	cases := []struct {
		jumps []int
		want  bool
	}{
		{[]int{2, 0, 3, 0, 0, 1}, true},
		{[]int{3, 1, 1, 0, 4}, false},
		{[]int{0}, true},
		{[]int{0, 1}, false},
		{[]int{4, 0, 0, 0, 0}, true},
		{[]int{1, 1, 0, 1}, false},
		{[]int{1, 2, 0, 0}, true},
	}
	for _, c := range cases {
		if got := CanReachLast(c.jumps); got != c.want {
			t.Errorf("CanReachLast(%v) = %v, want %v", c.jumps, got, c.want)
		}
	}
}

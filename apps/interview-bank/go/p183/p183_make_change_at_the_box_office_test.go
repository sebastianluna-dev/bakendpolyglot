package p183

import "testing"

func TestCanMakeChange(t *testing.T) {
	cases := []struct {
		bills []int
		want  bool
	}{
		{[]int{5, 5, 10, 5, 20}, true},
		{[]int{5, 10, 10, 20}, false},
		{[]int{}, true},
		{[]int{10}, false},
		{[]int{5, 5, 20}, false},
		{[]int{5, 5, 5, 20}, true},
		{[]int{5, 5, 5, 5, 10, 20, 10}, true},
	}
	for _, c := range cases {
		if got := CanMakeChange(c.bills); got != c.want {
			t.Errorf("CanMakeChange(%v) = %v, want %v", c.bills, got, c.want)
		}
	}
}

package p049

import (
	"slices"
	"testing"
)

func TestPushZerosToEnd(t *testing.T) {
	cases := []struct {
		slots []int
		want  []int
	}{
		{[]int{0, 41, 0, 0, 17, 23}, []int{41, 17, 23, 0, 0, 0}},
		{[]int{5, 0, 3}, []int{5, 3, 0}},
		{[]int{}, []int{}},
		{[]int{0, 0}, []int{0, 0}},
		{[]int{1, 2}, []int{1, 2}},
		{[]int{0, 0, 1}, []int{1, 0, 0}},
	}
	for _, c := range cases {
		PushZerosToEnd(c.slots)
		if !slices.Equal(c.slots, c.want) {
			t.Errorf("PushZerosToEnd left %v, want %v", c.slots, c.want)
		}
	}
}

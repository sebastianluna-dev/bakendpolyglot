package p085

import (
	"slices"
	"testing"
)

func fromSlice(values []int) *ListNode {
	var head *ListNode
	for i := len(values) - 1; i >= 0; i-- {
		head = &ListNode{Val: values[i], Next: head}
	}
	return head
}

func toSlice(head *ListNode) []int {
	values := []int{}
	for node := head; node != nil; node = node.Next {
		values = append(values, node.Val)
	}
	return values
}

func TestDropRepeats(t *testing.T) {
	cases := []struct {
		list []int
		want []int
	}{
		{[]int{1, 1, 2, 3, 3, 3}, []int{1, 2, 3}},
		{[]int{4, 4, 4}, []int{4}},
		{[]int{}, []int{}},
		{[]int{1, 2, 3}, []int{1, 2, 3}},
		{[]int{-2, -2, 0, 5, 5}, []int{-2, 0, 5}},
		{[]int{8}, []int{8}},
	}
	for _, c := range cases {
		if got := toSlice(DropRepeats(fromSlice(c.list))); !slices.Equal(got, c.want) {
			t.Errorf("DropRepeats(%v) = %v, want %v", c.list, got, c.want)
		}
	}
}

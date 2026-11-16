package p084

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

func TestMiddleNode(t *testing.T) {
	cases := []struct {
		list []int
		want []int // the list from the middle node on
	}{
		{[]int{3, 1, 4, 1, 5}, []int{4, 1, 5}},
		{[]int{2, 7, 1, 8}, []int{1, 8}},
		{[]int{6}, []int{6}},
		{[]int{}, []int{}},
		{[]int{9, 4}, []int{4}},
	}
	for _, c := range cases {
		if got := toSlice(MiddleNode(fromSlice(c.list))); !slices.Equal(got, c.want) {
			t.Errorf("MiddleNode(%v) starts %v, want %v", c.list, got, c.want)
		}
	}

	head := fromSlice([]int{10, 20, 30})
	if MiddleNode(head) != head.Next {
		t.Errorf("MiddleNode must return the node itself, not a copy")
	}
}

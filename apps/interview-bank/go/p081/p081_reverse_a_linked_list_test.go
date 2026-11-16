package p081

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

func TestReverseList(t *testing.T) {
	cases := []struct {
		list []int
		want []int
	}{
		{[]int{4, 8, 15, 16, 23}, []int{23, 16, 15, 8, 4}},
		{[]int{2, 9}, []int{9, 2}},
		{[]int{7}, []int{7}},
		{[]int{}, []int{}},
		{[]int{5, 5, 1}, []int{1, 5, 5}},
	}
	for _, c := range cases {
		if got := toSlice(ReverseList(fromSlice(c.list))); !slices.Equal(got, c.want) {
			t.Errorf("ReverseList(%v) = %v, want %v", c.list, got, c.want)
		}
	}
}

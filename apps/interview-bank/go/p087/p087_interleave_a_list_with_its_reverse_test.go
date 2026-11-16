package p087

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

func TestInterleaveWithReverse(t *testing.T) {
	cases := []struct {
		list []int
		want []int
	}{
		{[]int{1, 2, 3, 4, 5, 6}, []int{1, 6, 2, 5, 3, 4}},
		{[]int{3, 8, 5, 2, 9}, []int{3, 9, 8, 2, 5}},
		{[]int{}, []int{}},
		{[]int{7}, []int{7}},
		{[]int{1, 2}, []int{1, 2}},
		{[]int{10, 20, 30}, []int{10, 30, 20}},
		{[]int{1, 2, 3, 4}, []int{1, 4, 2, 3}},
	}
	for _, c := range cases {
		head := fromSlice(c.list)
		InterleaveWithReverse(head)
		if got := toSlice(head); !slices.Equal(got, c.want) {
			t.Errorf("InterleaveWithReverse(%v) left %v, want %v", c.list, got, c.want)
		}
	}
}

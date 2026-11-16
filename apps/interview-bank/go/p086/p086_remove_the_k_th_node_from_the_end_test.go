package p086

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

func TestRemoveKthFromEnd(t *testing.T) {
	cases := []struct {
		list []int
		k    int
		want []int
	}{
		{[]int{10, 20, 30, 40, 50}, 2, []int{10, 20, 30, 50}},
		{[]int{8}, 1, []int{}},
		{[]int{1, 2}, 2, []int{2}},
		{[]int{1, 2}, 1, []int{1}},
		{[]int{5, 6, 7}, 3, []int{6, 7}},
		{[]int{5, 6, 7}, 1, []int{5, 6}},
	}
	for _, c := range cases {
		if got := toSlice(RemoveKthFromEnd(fromSlice(c.list), c.k)); !slices.Equal(got, c.want) {
			t.Errorf("RemoveKthFromEnd(%v, %d) = %v, want %v", c.list, c.k, got, c.want)
		}
	}
}

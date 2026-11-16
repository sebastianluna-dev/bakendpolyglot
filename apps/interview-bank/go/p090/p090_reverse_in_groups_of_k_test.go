package p090

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

func TestReverseInGroups(t *testing.T) {
	cases := []struct {
		list []int
		k    int
		want []int
	}{
		{[]int{1, 2, 3, 4, 5, 6, 7, 8}, 3, []int{3, 2, 1, 6, 5, 4, 7, 8}},
		{[]int{1, 2, 3, 4, 5, 6}, 2, []int{2, 1, 4, 3, 6, 5}},
		{[]int{4, 5, 6}, 1, []int{4, 5, 6}},
		{[]int{4, 5, 6}, 3, []int{6, 5, 4}},
		{[]int{4, 5, 6}, 4, []int{4, 5, 6}},
		{[]int{}, 2, []int{}},
	}
	for _, c := range cases {
		if got := toSlice(ReverseInGroups(fromSlice(c.list), c.k)); !slices.Equal(got, c.want) {
			t.Errorf("ReverseInGroups(%v, %d) = %v, want %v", c.list, c.k, got, c.want)
		}
	}

	head := fromSlice([]int{1, 2, 3})
	if ReverseInGroups(head, 3).Next.Next != head {
		t.Errorf("ReverseInGroups must move the nodes, not rewrite their values")
	}
}

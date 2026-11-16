package p150

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

func TestMergeLists(t *testing.T) {
	cases := []struct {
		lists [][]int
		want  []int
	}{
		{[][]int{{1, 4, 7}, {2, 5}, {0, 6, 9}}, []int{0, 1, 2, 4, 5, 6, 7, 9}},
		{[][]int{{}, {3}, {}}, []int{3}},
		{[][]int{}, []int{}},
		{[][]int{{}}, []int{}},
		{[][]int{{2, 2}, {2}, {1, 2}}, []int{1, 2, 2, 2, 2}},
		{[][]int{{-5, 0, 5}}, []int{-5, 0, 5}},
		{[][]int{{10}, {-10}, {0}, {-20, 20}}, []int{-20, -10, 0, 10, 20}},
	}
	for _, c := range cases {
		heads := make([]*ListNode, len(c.lists))
		for i, l := range c.lists {
			heads[i] = fromSlice(l)
		}
		if got := toSlice(MergeLists(heads)); !slices.Equal(got, c.want) {
			t.Errorf("MergeLists(%v) = %v, want %v", c.lists, got, c.want)
		}
	}
}

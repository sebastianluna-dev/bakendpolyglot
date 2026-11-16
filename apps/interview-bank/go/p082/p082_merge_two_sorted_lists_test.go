package p082

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

func TestMergeSorted(t *testing.T) {
	cases := []struct {
		a, b []int
		want []int
	}{
		{[]int{1, 4, 6}, []int{2, 3, 6, 9}, []int{1, 2, 3, 4, 6, 6, 9}},
		{[]int{}, []int{5, 8}, []int{5, 8}},
		{[]int{}, []int{}, []int{}},
		{[]int{7, 8}, []int{1, 2}, []int{1, 2, 7, 8}},
		{[]int{-3, 0}, []int{-3}, []int{-3, -3, 0}},
	}
	for _, c := range cases {
		if got := toSlice(MergeSorted(fromSlice(c.a), fromSlice(c.b))); !slices.Equal(got, c.want) {
			t.Errorf("MergeSorted(%v, %v) = %v, want %v", c.a, c.b, got, c.want)
		}
	}

	first, second := fromSlice([]int{2}), fromSlice([]int{2})
	if merged := MergeSorted(first, second); merged != first || merged.Next != second {
		t.Errorf("on a tie, the node from the first list must come first")
	}
}

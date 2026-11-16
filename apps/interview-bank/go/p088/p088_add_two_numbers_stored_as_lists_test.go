package p088

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

func TestAddLists(t *testing.T) {
	nines, zerosThenOne := make([]int, 20), make([]int, 21)
	for i := range nines {
		nines[i] = 9
	}
	zerosThenOne[20] = 1

	cases := []struct {
		a, b []int
		want []int
	}{
		{[]int{5, 2, 9}, []int{8, 4}, []int{3, 7, 9}},
		{[]int{9, 9, 9}, []int{1}, []int{0, 0, 0, 1}},
		{[]int{0}, []int{0}, []int{0}},
		{[]int{1}, []int{6, 0, 3}, []int{7, 0, 3}},
		{[]int{9, 9}, []int{9, 9}, []int{8, 9, 1}},
		{nines, []int{1}, zerosThenOne},
	}
	for _, c := range cases {
		if got := toSlice(AddLists(fromSlice(c.a), fromSlice(c.b))); !slices.Equal(got, c.want) {
			t.Errorf("AddLists(%v, %v) = %v, want %v", c.a, c.b, got, c.want)
		}
	}
}

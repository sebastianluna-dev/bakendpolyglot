package p083

import "testing"

// withLoop builds the list and links its last node to the node at index loopTo (-1: no loop).
func withLoop(values []int, loopTo int) *ListNode {
	if len(values) == 0 {
		return nil
	}
	nodes := make([]*ListNode, len(values))
	for i, v := range values {
		nodes[i] = &ListNode{Val: v}
	}
	for i := 0; i+1 < len(nodes); i++ {
		nodes[i].Next = nodes[i+1]
	}
	if loopTo >= 0 {
		nodes[len(nodes)-1].Next = nodes[loopTo]
	}
	return nodes[0]
}

func TestHasLoop(t *testing.T) {
	cases := []struct {
		values []int
		loopTo int
		want   bool
	}{
		{[]int{3, 8, 1, 6}, 1, true},
		{[]int{4, 2}, -1, false},
		{[]int{5}, 0, true},
		{[]int{}, -1, false},
		{[]int{2, 5, 2}, -1, false},
		{[]int{1, 2, 3, 4, 5, 6}, 0, true},
		{[]int{7, 1}, 1, true},
	}
	for _, c := range cases {
		if got := HasLoop(withLoop(c.values, c.loopTo)); got != c.want {
			t.Errorf("HasLoop(%v looping to %d) = %v, want %v", c.values, c.loopTo, got, c.want)
		}
	}
}

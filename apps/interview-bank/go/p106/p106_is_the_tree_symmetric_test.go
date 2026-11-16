package p106

import (
	"math"
	"testing"
)

const null = math.MinInt // marks a missing child in a level-order slice

func fromLevelOrder(values []int) *TreeNode {
	if len(values) == 0 || values[0] == null {
		return nil
	}
	root := &TreeNode{Val: values[0]}
	queue := []*TreeNode{root}
	for i := 1; i < len(values); i += 2 {
		node := queue[0]
		queue = queue[1:]
		if values[i] != null {
			node.Left = &TreeNode{Val: values[i]}
			queue = append(queue, node.Left)
		}
		if i+1 < len(values) && values[i+1] != null {
			node.Right = &TreeNode{Val: values[i+1]}
			queue = append(queue, node.Right)
		}
	}
	return root
}

func TestIsSymmetric(t *testing.T) {
	cases := []struct {
		tree []int
		want bool
	}{
		{[]int{7, 4, 4, 1, 9, 9, 1}, true},
		{[]int{5, 3, 3, 8, null, null, 2}, false},
		{[]int{}, true},
		{[]int{1}, true},
		{[]int{2, 3, 3, null, 4, null, 4}, false},
		{[]int{1, 2, 3}, false},
	}
	for _, c := range cases {
		if got := IsSymmetric(fromLevelOrder(c.tree)); got != c.want {
			t.Errorf("IsSymmetric(%v) = %t, want %t", c.tree, got, c.want)
		}
	}
}

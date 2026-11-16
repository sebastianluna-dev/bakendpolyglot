package p107

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

func TestIsBalanced(t *testing.T) {
	cases := []struct {
		tree []int
		want bool
	}{
		{[]int{10, 5, 15, null, null, 12, 20}, true},
		{[]int{10, 5, null, 3, null, 1}, false},
		{[]int{}, true},
		{[]int{1, 2, 3, 4, null, null, 5, 6, null, null, 7}, false},
		{[]int{1, 2, 3, 4}, true},
		{[]int{1, 2}, true},
	}
	for _, c := range cases {
		if got := IsBalanced(fromLevelOrder(c.tree)); got != c.want {
			t.Errorf("IsBalanced(%v) = %t, want %t", c.tree, got, c.want)
		}
	}
}

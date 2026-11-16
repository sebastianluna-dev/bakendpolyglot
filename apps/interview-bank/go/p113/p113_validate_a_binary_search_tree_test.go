package p113

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

func TestIsSearchTree(t *testing.T) {
	cases := []struct {
		tree []int
		want bool
	}{
		{[]int{8, 4, 12, 2, 6, 10, 14}, true},
		{[]int{8, 4, 12, 2, 9, 10, 14}, false},
		{[]int{}, true},
		{[]int{5, 5}, false},
		{[]int{math.MinInt32, null, math.MaxInt32}, true},
		{[]int{math.MaxInt32, math.MaxInt32}, false},
		{[]int{math.MinInt32, math.MinInt32}, false},
	}
	for _, c := range cases {
		if got := IsSearchTree(fromLevelOrder(c.tree)); got != c.want {
			t.Errorf("IsSearchTree(%v) = %t, want %t", c.tree, got, c.want)
		}
	}
}

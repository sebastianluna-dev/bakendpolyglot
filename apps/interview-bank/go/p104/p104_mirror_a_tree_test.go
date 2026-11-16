package p104

import (
	"math"
	"slices"
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

func toLevelOrder(root *TreeNode) []int {
	if root == nil {
		return []int{}
	}
	values := []int{root.Val}
	queue := []*TreeNode{root}
	for len(queue) > 0 {
		node := queue[0]
		queue = queue[1:]
		for _, child := range []*TreeNode{node.Left, node.Right} {
			if child == nil {
				values = append(values, null)
			} else {
				values = append(values, child.Val)
				queue = append(queue, child)
			}
		}
	}
	for values[len(values)-1] == null {
		values = values[:len(values)-1]
	}
	return values
}

func TestMirrorTree(t *testing.T) {
	cases := []struct {
		tree []int
		want []int
	}{
		{[]int{6, 2, 8, 1, 4, null, 9}, []int{6, 8, 2, 9, null, 4, 1}},
		{[]int{3, 1}, []int{3, null, 1}},
		{[]int{}, []int{}},
		{[]int{7}, []int{7}},
		{[]int{1, 2, null, 3}, []int{1, null, 2, null, 3}},
	}
	for _, c := range cases {
		if got := toLevelOrder(MirrorTree(fromLevelOrder(c.tree))); !slices.Equal(got, c.want) {
			t.Errorf("MirrorTree(%v) = %v, want %v", c.tree, got, c.want)
		}
	}
}

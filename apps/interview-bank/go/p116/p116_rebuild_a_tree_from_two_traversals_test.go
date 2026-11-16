package p116

import (
	"math"
	"slices"
	"testing"
)

const null = math.MinInt // marks a missing child in a level-order slice

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

func TestBuildTree(t *testing.T) {
	cases := []struct {
		preorder []int
		inorder  []int
		want     []int
	}{
		{[]int{8, 4, 2, 6, 12, 14}, []int{2, 4, 6, 8, 12, 14}, []int{8, 4, 12, 2, 6, null, 14}},
		{[]int{1, 2, 3}, []int{3, 2, 1}, []int{1, 2, null, 3}},
		{[]int{}, []int{}, []int{}},
		{[]int{5}, []int{5}, []int{5}},
		{[]int{1, 2, 3}, []int{1, 2, 3}, []int{1, null, 2, null, 3}},
		{[]int{-1, -3, 0}, []int{-3, -1, 0}, []int{-1, -3, 0}},
	}
	for _, c := range cases {
		if got := toLevelOrder(BuildTree(c.preorder, c.inorder)); !slices.Equal(got, c.want) {
			t.Errorf("BuildTree(%v, %v) = %v, want %v", c.preorder, c.inorder, got, c.want)
		}
	}
}

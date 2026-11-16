package p120

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

func TestSerialize(t *testing.T) {
	cases := []struct {
		tree []int
		want string
	}{
		{[]int{7, 3, 9, null, 4, 8}, "7,3,#,4,#,#,9,8,#,#,#"},
		{[]int{}, "#"},
	}
	for _, c := range cases {
		if got := Serialize(fromLevelOrder(c.tree)); got != c.want {
			t.Errorf("Serialize(%v) = %q, want %q", c.tree, got, c.want)
		}
	}
}

func TestDeserialize(t *testing.T) {
	if got := toLevelOrder(Deserialize("5,-2,#,#,#")); !slices.Equal(got, []int{5, -2}) {
		t.Errorf(`Deserialize("5,-2,#,#,#") = %v, want [5 -2]`, got)
	}
}

func TestRoundTrip(t *testing.T) {
	trees := [][]int{{7, 3, 9, null, 4, 8}, {}, {-1000}, {1, null, 2, null, 3}, {10, -20, 30, -40, 50, -60, 70}}
	for _, tree := range trees {
		if got := toLevelOrder(Deserialize(Serialize(fromLevelOrder(tree)))); !slices.Equal(got, tree) {
			t.Errorf("round trip of %v = %v", tree, got)
		}
	}
}

// 114 · The k-th Smallest Key in a BST
package p114

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func KthSmallest(root *TreeNode, k int) int {
	var stack []*TreeNode
	node := root
	for {
		for node != nil {
			stack = append(stack, node)
			node = node.Left
		}
		smallest := stack[len(stack)-1] // the smallest key not yet counted
		stack = stack[:len(stack)-1]
		k--
		if k == 0 {
			return smallest.Val
		}
		node = smallest.Right
	}
}

// 108 · Longest Path Between Two Nodes
package p108

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func LongestPath(root *TreeNode) int {
	best := 0
	// Returns the depth in nodes, which is also the number of edges from the parent down.
	var depth func(node *TreeNode) int
	depth = func(node *TreeNode) int {
		if node == nil {
			return 0
		}
		left, right := depth(node.Left), depth(node.Right)
		best = max(best, left+right)
		return 1 + max(left, right)
	}
	depth(root)
	return best
}

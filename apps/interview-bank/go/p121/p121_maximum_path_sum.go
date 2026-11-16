// 121 · Maximum Path Sum
package p121

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func MaxPathSum(root *TreeNode) int {
	best := root.Val
	// returns the best sum of a path that starts at node and goes down, or 0 for no node
	var gain func(node *TreeNode) int
	gain = func(node *TreeNode) int {
		if node == nil {
			return 0
		}
		left := max(0, gain(node.Left)) // a negative branch is better left out
		right := max(0, gain(node.Right))
		best = max(best, node.Val+left+right)
		return node.Val + max(left, right)
	}
	gain(root)
	return best
}

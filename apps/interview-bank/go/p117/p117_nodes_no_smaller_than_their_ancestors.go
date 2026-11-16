// 117 · Nodes No Smaller Than Their Ancestors
package p117

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func CountGoodNodes(root *TreeNode) int {
	if root == nil {
		return 0
	}
	return countFrom(root, root.Val)
}

// highest is the largest value on the path from the root down to node's parent
func countFrom(node *TreeNode, highest int) int {
	if node == nil {
		return 0
	}
	good := 0
	if node.Val >= highest {
		good = 1
	}
	next := max(highest, node.Val)
	return good + countFrom(node.Left, next) + countFrom(node.Right, next)
}

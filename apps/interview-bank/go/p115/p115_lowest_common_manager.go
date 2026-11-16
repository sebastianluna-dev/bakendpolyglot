// 115 · Lowest Common Manager
package p115

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func LowestCommonManager(root *TreeNode, a, b int) int {
	return lowest(root, a, b).Val
}

// returns the answer if both ids are below node, else whichever of the two is below it, else nil
func lowest(node *TreeNode, a, b int) *TreeNode {
	if node == nil || node.Val == a || node.Val == b {
		return node
	}
	left := lowest(node.Left, a, b)
	right := lowest(node.Right, a, b)
	if left != nil && right != nil {
		return node
	}
	if left != nil {
		return left
	}
	return right
}

// 107 · Is the Tree Height-Balanced?
package p107

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func IsBalanced(root *TreeNode) bool {
	return balancedHeight(root) != -1
}

// The height of the subtree, or -1 as soon as any subtree in it is unbalanced.
func balancedHeight(node *TreeNode) int {
	if node == nil {
		return 0
	}
	left := balancedHeight(node.Left)
	if left == -1 {
		return -1
	}
	right := balancedHeight(node.Right)
	if right == -1 || left-right > 1 || right-left > 1 {
		return -1
	}
	return 1 + max(left, right)
}

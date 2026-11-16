// 110 · Sum of a Key Range in a BST
package p110

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func RangeSum(root *TreeNode, lo, hi int) int {
	if root == nil {
		return 0
	}
	if root.Val < lo {
		return RangeSum(root.Right, lo, hi) // the left subtree is even smaller
	}
	if root.Val > hi {
		return RangeSum(root.Left, lo, hi)
	}
	return root.Val + RangeSum(root.Left, lo, hi) + RangeSum(root.Right, lo, hi)
}

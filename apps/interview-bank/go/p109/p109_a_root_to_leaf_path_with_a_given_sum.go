// 109 · A Root-to-Leaf Path With a Given Sum
package p109

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func HasPathSum(root *TreeNode, target int) bool {
	if root == nil {
		return false
	}
	if root.Left == nil && root.Right == nil {
		return root.Val == target
	}
	rest := target - root.Val
	return HasPathSum(root.Left, rest) || HasPathSum(root.Right, rest)
}

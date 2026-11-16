// 105 · Are Two Trees Identical?
package p105

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func AreIdentical(a, b *TreeNode) bool {
	if a == nil || b == nil {
		return a == b
	}
	return a.Val == b.Val && AreIdentical(a.Left, b.Left) && AreIdentical(a.Right, b.Right)
}

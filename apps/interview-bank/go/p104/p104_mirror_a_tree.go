// 104 · Mirror a Tree
package p104

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func MirrorTree(root *TreeNode) *TreeNode {
	if root == nil {
		return nil
	}
	root.Left, root.Right = MirrorTree(root.Right), MirrorTree(root.Left)
	return root
}

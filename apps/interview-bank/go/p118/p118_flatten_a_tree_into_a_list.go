// 118 · Flatten a Tree Into a List
package p118

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func Flatten(root *TreeNode) {
	for node := root; node != nil; node = node.Right {
		if node.Left != nil {
			tail := node.Left // the last node of the left subtree in preorder
			for tail.Right != nil {
				tail = tail.Right
			}
			tail.Right = node.Right
			node.Right = node.Left
			node.Left = nil
		}
	}
}

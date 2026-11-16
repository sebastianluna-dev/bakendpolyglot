// 113 · Validate a Binary Search Tree
package p113

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func IsSearchTree(root *TreeNode) bool {
	return within(root, nil, nil)
}

// lo and hi are the nearest ancestors the key must stay above and below; nil means no bound
func within(node, lo, hi *TreeNode) bool {
	if node == nil {
		return true
	}
	if lo != nil && node.Val <= lo.Val {
		return false
	}
	if hi != nil && node.Val >= hi.Val {
		return false
	}
	return within(node.Left, lo, node) && within(node.Right, node, hi)
}

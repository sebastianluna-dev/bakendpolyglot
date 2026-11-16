// 111 · Level-by-Level Traversal
package p111

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func LevelOrder(root *TreeNode) [][]int {
	levels := [][]int{}
	var level []*TreeNode
	if root != nil {
		level = []*TreeNode{root}
	}
	for len(level) > 0 {
		var next []*TreeNode
		values := make([]int, 0, len(level))
		for _, node := range level {
			values = append(values, node.Val)
			if node.Left != nil {
				next = append(next, node.Left)
			}
			if node.Right != nil {
				next = append(next, node.Right)
			}
		}
		levels = append(levels, values)
		level = next
	}
	return levels
}

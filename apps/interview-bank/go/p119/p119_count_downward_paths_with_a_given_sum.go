// 119 · Count Downward Paths With a Given Sum
package p119

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func CountPathsWithSum(root *TreeNode, target int) int {
	seen := map[int64]int{0: 1} // prefix sums on the current root path -> count
	var visit func(node *TreeNode, prefix int64) int
	visit = func(node *TreeNode, prefix int64) int {
		if node == nil {
			return 0
		}
		total := prefix + int64(node.Val)
		count := seen[total-int64(target)]
		seen[total]++
		count += visit(node.Left, total) + visit(node.Right, total)
		seen[total]--
		return count
	}
	return visit(root, 0)
}

// 189 · Can You Reach the Last Square?
package p189

func CanReachLast(jumps []int) bool {
	furthest := 0
	for i, jump := range jumps {
		if i > furthest {
			return false // no reachable square jumps this far
		}
		furthest = max(furthest, i+jump)
	}
	return true
}

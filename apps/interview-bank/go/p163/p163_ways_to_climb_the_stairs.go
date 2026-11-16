// 163 · Ways to Climb the Stairs
package p163

func WaysToClimb(n int) int64 {
	var prev, cur int64 = 1, 1 // ways to reach steps i - 2 and i - 1
	for i := 2; i <= n; i++ {
		prev, cur = cur, prev+cur
	}
	return cur
}

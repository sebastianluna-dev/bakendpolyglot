// 025 · Most Points on One Line
package p025

func MaxPointsOnLine(points [][]int) int {
	best := 0
	for i := range points {
		directions := map[[2]int]int{} // reduced direction -> points after i on it
		same := 1                      // point i and its copies
		most := 0
		for j := i + 1; j < len(points); j++ {
			dx := points[j][0] - points[i][0]
			dy := points[j][1] - points[i][1]
			if dx == 0 && dy == 0 {
				same++
				continue
			}
			g := gcd(abs(dx), abs(dy))
			dx /= g
			dy /= g
			// (1, -1) and (-1, 1) are the same line: make dx positive, or dy if dx is 0
			if dx < 0 || (dx == 0 && dy < 0) {
				dx, dy = -dx, -dy
			}
			key := [2]int{dx, dy}
			directions[key]++
			most = max(most, directions[key])
		}
		best = max(best, same+most)
	}
	return best
}

func gcd(a, b int) int {
	for b != 0 {
		a, b = b, a%b
	}
	return a
}

func abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}

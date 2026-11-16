// 168 · Paint the Houses for Less
package p168

func MinPaintCost(costs [][]int) int {
	red, green, blue := 0, 0, 0 // cheapest totals so far, by the colour of the last house
	for _, c := range costs {
		red, green, blue = c[0]+min(green, blue), c[1]+min(red, blue), c[2]+min(red, green)
	}
	return min(red, green, blue)
}

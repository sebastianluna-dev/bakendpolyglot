// 021 · Read a Matrix in a Spiral
package p021

func SpiralOrder(grid [][]int) []int {
	result := []int{}
	if len(grid) == 0 {
		return result
	}
	top, bottom, left, right := 0, len(grid)-1, 0, len(grid[0])-1
	for top <= bottom && left <= right {
		for c := left; c <= right; c++ {
			result = append(result, grid[top][c])
		}
		top++
		for r := top; r <= bottom; r++ {
			result = append(result, grid[r][right])
		}
		right--
		// after the two steps above, a single row or column may be used up
		if top <= bottom {
			for c := right; c >= left; c-- {
				result = append(result, grid[bottom][c])
			}
			bottom--
		}
		if left <= right {
			for r := bottom; r >= top; r-- {
				result = append(result, grid[r][left])
			}
			left++
		}
	}
	return result
}

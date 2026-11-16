// 135 · Shortest Clear Path in a Grid
package p135

func ShortestClearPath(grid [][]int) int {
	rows, cols := len(grid), len(grid[0])
	if grid[0][0] == 1 || grid[rows-1][cols-1] == 1 {
		return -1
	}
	cells := make([][]int, rows) // 0 = not reached
	for r := range cells {
		cells[r] = make([]int, cols)
	}
	cells[0][0] = 1
	queue := [][2]int{{0, 0}}
	for len(queue) > 0 {
		r, c := queue[0][0], queue[0][1]
		queue = queue[1:]
		if r == rows-1 && c == cols-1 {
			return cells[r][c]
		}
		for dr := -1; dr <= 1; dr++ {
			for dc := -1; dc <= 1; dc++ {
				nr, nc := r+dr, c+dc
				if nr < 0 || nr >= rows || nc < 0 || nc >= cols {
					continue
				}
				if grid[nr][nc] == 1 || cells[nr][nc] != 0 {
					continue // also skips (r, c) itself
				}
				cells[nr][nc] = cells[r][c] + 1
				queue = append(queue, [2]int{nr, nc})
			}
		}
	}
	return -1
}

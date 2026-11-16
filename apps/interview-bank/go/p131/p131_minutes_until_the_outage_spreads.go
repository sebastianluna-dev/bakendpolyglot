// 131 · Minutes Until the Outage Spreads
package p131

var dirs = [4][2]int{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}

func MinutesUntilAllFail(grid [][]int) int {
	rows, cols := len(grid), len(grid[0])
	queue := [][2]int{}
	healthy := 0
	for r := range rows {
		for c := range cols {
			if grid[r][c] == 2 {
				queue = append(queue, [2]int{r, c})
			} else if grid[r][c] == 1 {
				healthy++
			}
		}
	}
	minutes := 0
	for len(queue) > 0 && healthy > 0 {
		minutes++
		for k := len(queue); k > 0; k-- { // one minute: only the servers that failed last minute
			cell := queue[0]
			queue = queue[1:]
			for _, d := range dirs {
				nr, nc := cell[0]+d[0], cell[1]+d[1]
				if nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] != 1 {
					continue
				}
				grid[nr][nc] = 2
				healthy--
				queue = append(queue, [2]int{nr, nc})
			}
		}
	}
	if healthy > 0 {
		return -1
	}
	return minutes
}

// 128 · Count the Islands
package p128

var dirs = [4][2]int{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}

func CountIslands(grid [][]int) int {
	rows, cols := len(grid), len(grid[0])
	seen := make([][]bool, rows)
	for r := range seen {
		seen[r] = make([]bool, cols)
	}
	islands := 0
	for sr := range rows {
		for sc := range cols {
			if grid[sr][sc] != 1 || seen[sr][sc] {
				continue
			}
			islands++
			seen[sr][sc] = true
			queue := [][2]int{{sr, sc}}
			for len(queue) > 0 {
				cell := queue[0]
				queue = queue[1:]
				for _, d := range dirs {
					nr, nc := cell[0]+d[0], cell[1]+d[1]
					if nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] != 1 || seen[nr][nc] {
						continue
					}
					seen[nr][nc] = true
					queue = append(queue, [2]int{nr, nc})
				}
			}
		}
	}
	return islands
}

// 127 · Perimeter of an Island
package p127

func IslandPerimeter(grid [][]int) int {
	perimeter := 0
	for r, row := range grid {
		for c, cell := range row {
			if cell != 1 {
				continue
			}
			perimeter += 4
			if r > 0 && grid[r-1][c] == 1 {
				perimeter -= 2 // the shared side is lost by both cells
			}
			if c > 0 && row[c-1] == 1 {
				perimeter -= 2
			}
		}
	}
	return perimeter
}

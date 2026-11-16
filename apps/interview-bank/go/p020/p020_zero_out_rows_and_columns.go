// 020 · Zero Out Rows and Columns
package p020

func ZeroOut(grid [][]int) {
	zeroRow := make([]bool, len(grid))
	zeroCol := make([]bool, len(grid[0]))
	for r, row := range grid {
		for c, v := range row {
			if v == 0 {
				zeroRow[r], zeroCol[c] = true, true
			}
		}
	}
	for r, row := range grid {
		for c := range row {
			if zeroRow[r] || zeroCol[c] {
				row[c] = 0
			}
		}
	}
}

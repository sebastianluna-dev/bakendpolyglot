// 162 · Solve a Sudoku
package p162

func SolveSudoku(board []string) []string {
	grid := make([][]byte, 9)
	for r := range grid {
		grid[r] = []byte(board[r])
	}
	var inRow, inCol, inBox [9][10]bool
	empty := [][2]int{}
	for r := range 9 {
		for c := range 9 {
			if grid[r][c] == '.' {
				empty = append(empty, [2]int{r, c})
			} else {
				d := grid[r][c] - '0'
				inRow[r][d], inCol[c][d], inBox[r/3*3+c/3][d] = true, true, true
			}
		}
	}
	var fill func(k int) bool
	fill = func(k int) bool {
		if k == len(empty) {
			return true
		}
		r, c := empty[k][0], empty[k][1]
		b := r/3*3 + c/3
		for d := 1; d <= 9; d++ {
			if inRow[r][d] || inCol[c][d] || inBox[b][d] {
				continue
			}
			inRow[r][d], inCol[c][d], inBox[b][d] = true, true, true
			grid[r][c] = byte('0' + d)
			if fill(k + 1) {
				return true // keep the digits: the board is solved
			}
			inRow[r][d], inCol[c][d], inBox[b][d] = false, false, false
			grid[r][c] = '.'
		}
		return false
	}
	if !fill(0) {
		return []string{}
	}
	solved := make([]string, 9)
	for r, row := range grid {
		solved[r] = string(row)
	}
	return solved
}

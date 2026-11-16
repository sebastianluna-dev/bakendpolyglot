// 019 · Validate a Sudoku Board
package p019

func IsValidSudoku(board []string) bool {
	var inRow, inCol, inBox [9][9]bool
	for r := 0; r < 9; r++ {
		for c := 0; c < 9; c++ {
			ch := board[r][c]
			if ch == '.' {
				continue
			}
			d := int(ch - '1')
			b := r/3*3 + c/3
			if inRow[r][d] || inCol[c][d] || inBox[b][d] {
				return false
			}
			inRow[r][d], inCol[c][d], inBox[b][d] = true, true, true
		}
	}
	return true
}

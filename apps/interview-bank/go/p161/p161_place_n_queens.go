// 161 · Place n Queens
package p161

import "strings"

func PlaceQueens(n int) [][]string {
	result := [][]string{}
	queens := []int{} // queens[r] is the column of the queen in row r
	cols := make([]bool, n)
	downs := make([]bool, 2*n-1) // r + c is the same along a down-left diagonal
	ups := make([]bool, 2*n-1)   // r - c + n - 1 is the same along a down-right diagonal
	var walk func(r int)
	walk = func(r int) {
		if r == n {
			board := make([]string, n)
			for row, c := range queens {
				board[row] = strings.Repeat(".", c) + "Q" + strings.Repeat(".", n-c-1)
			}
			result = append(result, board)
			return
		}
		for c := range n {
			if cols[c] || downs[r+c] || ups[r-c+n-1] {
				continue
			}
			cols[c], downs[r+c], ups[r-c+n-1] = true, true, true
			queens = append(queens, c)
			walk(r + 1)
			queens = queens[:len(queens)-1]
			cols[c], downs[r+c], ups[r-c+n-1] = false, false, false
		}
	}
	walk(0)
	return result
}

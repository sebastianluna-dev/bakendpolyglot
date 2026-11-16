// 158 · Find a Word in a Letter Grid
package p158

func FindWord(grid []string, word string) bool {
	rows, cols := len(grid), len(grid[0])
	used := make([][]bool, rows)
	for r := range used {
		used[r] = make([]bool, cols)
	}
	var walk func(r, c, k int) bool
	walk = func(r, c, k int) bool {
		if k == len(word) {
			return true
		}
		if r < 0 || r >= rows || c < 0 || c >= cols || used[r][c] || grid[r][c] != word[k] {
			return false
		}
		used[r][c] = true
		found := walk(r+1, c, k+1) || walk(r-1, c, k+1) || walk(r, c+1, k+1) || walk(r, c-1, k+1)
		used[r][c] = false // free the cell for paths that start elsewhere
		return found
	}
	for r := range rows {
		for c := range cols {
			if walk(r, c, 0) {
				return true
			}
		}
	}
	return false
}

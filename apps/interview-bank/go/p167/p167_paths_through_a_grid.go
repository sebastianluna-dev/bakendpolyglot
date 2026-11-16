// 167 · Paths Through a Grid
package p167

func GridPaths(rows, cols int) int {
	row := make([]int, cols)
	for c := range row {
		row[c] = 1 // the top row: one path to each cell
	}
	for r := 1; r < rows; r++ {
		for c := 1; c < cols; c++ {
			row[c] += row[c-1] // from above (old row[c]) plus from the left
		}
	}
	return row[cols-1]
}

// 010 · Number Triangle Rows
package p010

func TriangleRows(n int) [][]int {
	rows := make([][]int, n)
	for r := range n {
		row := make([]int, r+1)
		row[0], row[r] = 1, 1
		for c := 1; c < r; c++ {
			row[c] = rows[r-1][c-1] + rows[r-1][c]
		}
		rows[r] = row
	}
	return rows
}

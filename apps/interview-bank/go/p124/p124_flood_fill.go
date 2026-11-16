// 124 · Flood Fill
package p124

var dirs = [4][2]int{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}

func FloodFill(image [][]int, row, col, color int) [][]int {
	old := image[row][col]
	if old == color {
		return image // nothing to do, and the loop below would never stop
	}
	rows, cols := len(image), len(image[0])
	image[row][col] = color
	queue := [][2]int{{row, col}}
	for len(queue) > 0 {
		cell := queue[0]
		queue = queue[1:]
		for _, d := range dirs {
			nr, nc := cell[0]+d[0], cell[1]+d[1]
			if nr < 0 || nr >= rows || nc < 0 || nc >= cols || image[nr][nc] != old {
				continue
			}
			image[nr][nc] = color
			queue = append(queue, [2]int{nr, nc})
		}
	}
	return image
}

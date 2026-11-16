// 132 · Water That Reaches Both Coasts
package p132

var dirs = [4][2]int{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}

func BothCoasts(heights [][]int) [][2]int {
	rows, cols := len(heights), len(heights[0])
	northWestEdge, southEastEdge := [][2]int{}, [][2]int{}
	for r := range rows {
		for c := range cols {
			if r == 0 || c == 0 {
				northWestEdge = append(northWestEdge, [2]int{r, c})
			}
			if r == rows-1 || c == cols-1 {
				southEastEdge = append(southEastEdge, [2]int{r, c})
			}
		}
	}
	northWest := reach(heights, northWestEdge)
	southEast := reach(heights, southEastEdge)
	cells := [][2]int{}
	for r := range rows {
		for c := range cols {
			if northWest[r][c] && southEast[r][c] {
				cells = append(cells, [2]int{r, c})
			}
		}
	}
	return cells
}

func reach(heights [][]int, starts [][2]int) [][]bool {
	rows, cols := len(heights), len(heights[0])
	seen := make([][]bool, rows)
	for r := range seen {
		seen[r] = make([]bool, cols)
	}
	for _, s := range starts {
		seen[s[0]][s[1]] = true
	}
	queue := starts
	for len(queue) > 0 {
		cell := queue[0]
		queue = queue[1:]
		r, c := cell[0], cell[1]
		for _, d := range dirs {
			nr, nc := r+d[0], c+d[1]
			// uphill or level: water from (nr, nc) could flow down to (r, c)
			if nr < 0 || nr >= rows || nc < 0 || nc >= cols || seen[nr][nc] || heights[nr][nc] < heights[r][c] {
				continue
			}
			seen[nr][nc] = true
			queue = append(queue, [2]int{nr, nc})
		}
	}
	return seen
}

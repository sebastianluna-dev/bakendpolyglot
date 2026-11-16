// 169 · Region Totals on a Seat Map
package p169

type RegionSums struct {
	prefix [][]int // prefix[r][c] = total of the rows above r and the columns left of c
}

func NewRegionSums(grid [][]int) *RegionSums {
	rows, cols := len(grid), len(grid[0])
	prefix := make([][]int, rows+1)
	for r := range prefix {
		prefix[r] = make([]int, cols+1)
	}
	for r := 0; r < rows; r++ {
		for c := 0; c < cols; c++ {
			prefix[r+1][c+1] = grid[r][c] + prefix[r][c+1] + prefix[r+1][c] - prefix[r][c]
		}
	}
	return &RegionSums{prefix: prefix}
}

func (s *RegionSums) Sum(r1, c1, r2, c2 int) int {
	p := s.prefix
	return p[r2+1][c2+1] - p[r1][c2+1] - p[r2+1][c1] + p[r1][c1]
}

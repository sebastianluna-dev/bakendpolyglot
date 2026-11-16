// 184 · Seat Everyone With the Fewest Moves
package p184

import "slices"

func FewestMoves(seats, guests []int) int {
	s := slices.Clone(seats)
	g := slices.Clone(guests)
	slices.Sort(s)
	slices.Sort(g)
	moves := 0
	for i := range s {
		moves += abs(s[i] - g[i])
	}
	return moves
}

func abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}

// 139 · Cheapest Route With at Most k Stops
package p139

import (
	"math"
	"slices"
)

func CheapestRoute(n int, legs [][]int, source, target, k int) int {
	cost := make([]int, n)
	for i := range cost {
		cost[i] = math.MaxInt
	}
	cost[source] = 0
	for round := 0; round <= k; round++ {
		next := slices.Clone(cost) // relax from last round's costs only, so each round adds at most one leg
		for _, leg := range legs {
			from, to, price := leg[0], leg[1], leg[2]
			if cost[from] != math.MaxInt && cost[from]+price < next[to] {
				next[to] = cost[from] + price
			}
		}
		cost = next
	}
	if cost[target] == math.MaxInt {
		return -1
	}
	return cost[target]
}

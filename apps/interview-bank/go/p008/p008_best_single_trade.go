// 008 · Best Single Trade
package p008

import "math"

func BestTrade(prices []int) int {
	lowest := math.MaxInt
	best := 0
	for _, price := range prices {
		lowest = min(lowest, price)
		best = max(best, price-lowest)
	}
	return best
}

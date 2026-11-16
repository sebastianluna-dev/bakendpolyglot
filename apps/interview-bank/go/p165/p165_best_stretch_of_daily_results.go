// 165 · Best Stretch of Daily Results
package p165

func BestStretch(results []int) int {
	endingHere, best := 0, results[0] // endingHere: best stretch that ends on day r
	for _, r := range results {
		endingHere = max(r, endingHere+r)
		best = max(best, endingHere)
	}
	return best
}

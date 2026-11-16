// 051 · Best Average Over k Days
package p051

func BestAverage(balances []int, k int) float64 {
	sum := 0
	for i := 0; i < k; i++ {
		sum += balances[i]
	}
	best := sum
	for i := k; i < len(balances); i++ {
		sum += balances[i] - balances[i-k] // the day entering minus the day leaving
		best = max(best, sum)
	}
	return float64(best) / float64(k)
}

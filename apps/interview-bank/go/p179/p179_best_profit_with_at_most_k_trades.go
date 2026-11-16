// 179 · Best Profit With at Most k Trades
package p179

func BestProfit(prices []int, k int) int {
	if len(prices) == 0 || k == 0 {
		return 0
	}
	bought := make([]int, k+1) // bought[t]: most cash holding the t-th share
	for t := range bought {
		bought[t] = -prices[0]
	}
	sold := make([]int, k+1) // sold[t]: most cash after t complete trades
	for _, p := range prices {
		for t := 1; t <= k; t++ {
			bought[t] = max(bought[t], sold[t-1]-p)
			sold[t] = max(sold[t], bought[t]+p)
		}
	}
	return sold[k]
}

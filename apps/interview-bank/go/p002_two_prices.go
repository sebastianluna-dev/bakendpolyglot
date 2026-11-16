// 002 · Two Prices for a Budget
package bank

func TwoPrices(prices []int, budget int) []int {
	firstIndex := map[int]int{} // price -> earliest index
	for j, p := range prices {
		if i, ok := firstIndex[budget-p]; ok {
			return []int{i, j}
		}
		if _, seen := firstIndex[p]; !seen {
			firstIndex[p] = j
		}
	}
	return []int{}
}

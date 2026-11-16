// 170 · Fewest Coins for an Amount
package p170

func FewestCoins(coins []int, amount int) int {
	unreachable := amount + 1 // more coins than any real answer needs
	fewest := make([]int, amount+1)
	for a := 1; a <= amount; a++ {
		fewest[a] = unreachable
		for _, coin := range coins {
			if coin <= a {
				fewest[a] = min(fewest[a], fewest[a-coin]+1)
			}
		}
	}
	if fewest[amount] == unreachable {
		return -1
	}
	return fewest[amount]
}

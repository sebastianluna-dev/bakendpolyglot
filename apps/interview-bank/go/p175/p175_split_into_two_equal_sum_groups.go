// 175 · Split Into Two Equal-Sum Groups
package p175

func CanSplitEvenly(weights []int) bool {
	total := 0
	for _, w := range weights {
		total += w
	}
	if total%2 != 0 {
		return false
	}
	half := total / 2
	reachable := make([]bool, half+1) // reachable[s]: some parcels seen so far weigh s
	reachable[0] = true
	for _, w := range weights {
		for s := half; s >= w; s-- { // downwards, so this parcel is counted at most once
			if reachable[s-w] {
				reachable[s] = true
			}
		}
	}
	return reachable[half]
}

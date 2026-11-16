// 102 · Split Parcels Across k Trucks With the Lightest Peak
package p102

func LightestPeak(weights []int, k int) int {
	lo, hi := 0, 0
	for _, w := range weights {
		lo = max(lo, w)
		hi += w
	}
	for lo < hi {
		mid := lo + (hi-lo)/2
		if trucksNeeded(weights, mid) <= k {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	return lo
}

func trucksNeeded(weights []int, limit int) int {
	trucks, load := 1, 0
	for _, w := range weights {
		if load+w > limit {
			trucks++
			load = 0
		}
		load += w
	}
	return trucks
}

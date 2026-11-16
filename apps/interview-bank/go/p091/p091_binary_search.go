// 091 · Binary Search
package p091

func BinarySearch(ids []int, target int) int {
	lo, hi := 0, len(ids) // the first ID >= target is somewhere in [lo, hi)
	for lo < hi {
		mid := lo + (hi-lo)/2
		if ids[mid] >= target {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	if lo < len(ids) && ids[lo] == target {
		return lo
	}
	return -1
}

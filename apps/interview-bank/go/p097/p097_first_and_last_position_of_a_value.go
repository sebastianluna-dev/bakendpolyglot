// 097 · First and Last Position of a Value
package p097

func FirstAndLast(days []int, day int) []int {
	first := firstAtLeast(days, day)
	if first == len(days) || days[first] != day {
		return []int{-1, -1}
	}
	return []int{first, firstAtLeast(days, day+1) - 1} // one before the first value >= day+1
}

func firstAtLeast(a []int, target int) int {
	lo, hi := 0, len(a)
	for lo < hi {
		mid := lo + (hi-lo)/2
		if a[mid] >= target {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	return lo
}

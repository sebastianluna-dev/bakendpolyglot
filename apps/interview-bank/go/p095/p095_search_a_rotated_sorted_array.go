// 095 · Search a Rotated Sorted Array
package p095

func SearchRotated(times []int, target int) int {
	n := len(times)
	if n == 0 {
		return -1
	}
	last := times[n-1]
	lo, hi := 0, n
	for lo < hi {
		mid := lo + (hi-lo)/2
		if times[mid] <= last {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	start := lo // index of the minimum: [start, n) and [0, start) are both sorted
	from, to := 0, start
	if target <= last {
		from, to = start, n
	}
	if i := firstAtLeast(times, from, to, target); i < to && times[i] == target {
		return i
	}
	return -1
}

func firstAtLeast(a []int, lo, hi, target int) int {
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

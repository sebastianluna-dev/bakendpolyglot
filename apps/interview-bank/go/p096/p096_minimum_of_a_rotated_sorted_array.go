// 096 · Minimum of a Rotated Sorted Array
package p096

func RotatedMinimum(times []int) int {
	last := times[len(times)-1]
	lo, hi := 0, len(times)
	for lo < hi {
		mid := lo + (hi-lo)/2
		// Values <= last belong to the run after the wrap, which starts at the minimum.
		if times[mid] <= last {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	return times[lo]
}

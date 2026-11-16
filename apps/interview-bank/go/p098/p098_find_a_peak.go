// 098 · Find a Peak
package p098

func FindPeak(altitudes []int) int {
	lo, hi := 0, len(altitudes)-1 // the last index can be the answer, but has no right neighbour to test
	for lo < hi {
		mid := lo + (hi-lo)/2
		// Going down after mid: there is a peak at mid or to its left.
		if altitudes[mid] > altitudes[mid+1] {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	return lo
}

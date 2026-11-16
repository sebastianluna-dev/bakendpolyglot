// 092 · Where Would It Go?
package p092

func InsertPosition(deadlines []int, deadline int) int {
	lo, hi := 0, len(deadlines) // the answer may be len(deadlines) itself
	for lo < hi {
		mid := lo + (hi-lo)/2
		if deadlines[mid] >= deadline {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	return lo
}

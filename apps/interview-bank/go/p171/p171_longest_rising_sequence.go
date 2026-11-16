// 171 · Longest Rising Sequence
package p171

func LongestRising(nums []int) int {
	tails := []int{} // tails[k] = smallest last value of a rising sequence of length k + 1
	for _, x := range nums {
		lo, hi := 0, len(tails)
		for lo < hi {
			// first tail >= x, so an equal value replaces instead of extending
			mid := (lo + hi) / 2
			if tails[mid] < x {
				lo = mid + 1
			} else {
				hi = mid
			}
		}
		if lo == len(tails) {
			tails = append(tails, x)
		} else {
			tails[lo] = x
		}
	}
	return len(tails)
}

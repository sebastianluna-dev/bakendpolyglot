// 061 · Longest Run of Ones With k Flips
package p061

func LongestOnes(seats []int, k int) int {
	left, zeros, best := 0, 0, 0 // zeros inside seats[left..right]
	for right, seat := range seats {
		if seat == 0 {
			zeros++
		}
		for zeros > k {
			if seats[left] == 0 {
				zeros--
			}
			left++
		}
		best = max(best, right-left+1)
	}
	return best
}

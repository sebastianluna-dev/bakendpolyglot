// 094 · Integer Square Root
package p094

func IntegerSqrt(x int) int {
	// int is 64 bits on 64-bit platforms, so mid*mid cannot overflow here
	lo, hi := 0, x+1 // looking for the first k with k*k > x; k = x+1 always is one
	for lo < hi {
		mid := lo + (hi-lo)/2
		if mid*mid > x {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	return lo - 1
}

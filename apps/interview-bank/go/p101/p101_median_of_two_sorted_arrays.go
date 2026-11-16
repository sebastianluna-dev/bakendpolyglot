// 101 · Median of Two Sorted Arrays
package p101

import "math"

func MedianOfTwoSorted(a, b []int) float64 {
	if len(a) > len(b) {
		return MedianOfTwoSorted(b, a)
	}
	m, n := len(a), len(b)
	half := (m + n + 1) / 2
	lo, hi := 0, m // m stands for "take all of a"
	for lo < hi {
		mid := lo + (hi-lo)/2 // values of a in the left half
		rest := half - mid
		if rest == 0 || b[rest-1] <= a[mid] {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	i := lo
	j := half - i
	leftMax, rightMin := math.MinInt, math.MaxInt
	if i > 0 {
		leftMax = a[i-1]
	}
	if j > 0 {
		leftMax = max(leftMax, b[j-1])
	}
	if (m+n)%2 == 1 {
		return float64(leftMax)
	}
	if i < m {
		rightMin = a[i]
	}
	if j < n {
		rightMin = min(rightMin, b[j])
	}
	return float64(leftMax+rightMin) / 2
}

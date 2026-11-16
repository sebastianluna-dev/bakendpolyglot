// 176 · Largest Product of a Stretch
package p176

func LargestProduct(nums []int) int {
	best := nums[0]
	hi := nums[0] // largest product of a stretch that ends at the current element
	lo := nums[0] // smallest one: a negative number turns it into the largest
	for _, x := range nums[1:] {
		a, b := hi*x, lo*x
		hi = max(x, a, b)
		lo = min(x, a, b)
		best = max(best, hi)
	}
	return best
}

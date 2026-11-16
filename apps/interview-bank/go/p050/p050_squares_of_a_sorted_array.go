// 050 · Squares of a Sorted Array
package p050

func SortedSquares(nums []int) []int {
	out := make([]int, len(nums))
	lo, hi := 0, len(nums)-1
	for k := len(nums) - 1; k >= 0; k-- {
		// the largest square left is at one of the two ends
		if abs(nums[lo]) > abs(nums[hi]) {
			out[k] = nums[lo] * nums[lo]
			lo++
		} else {
			out[k] = nums[hi] * nums[hi]
			hi--
		}
	}
	return out
}

func abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}

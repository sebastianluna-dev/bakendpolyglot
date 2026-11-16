// 016 · Product of Everything Else
package p016

func ProductOfOthers(nums []int) []int {
	out := make([]int, len(nums))
	left := 1
	for i, x := range nums {
		out[i] = left // product of everything before i
		left *= x
	}
	right := 1
	for i := len(nums) - 1; i >= 0; i-- {
		out[i] *= right
		right *= nums[i]
	}
	return out
}

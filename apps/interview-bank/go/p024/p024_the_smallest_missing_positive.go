// 024 · The Smallest Missing Positive
package p024

func SmallestMissingPositive(nums []int) int {
	n := len(nums)
	for i := range nums {
		// keep swapping until slot i holds a value that has no slot or is already in its slot
		for nums[i] >= 1 && nums[i] <= n && nums[nums[i]-1] != nums[i] {
			j := nums[i] - 1
			nums[i], nums[j] = nums[j], nums[i]
		}
	}
	for i, v := range nums {
		if v != i+1 {
			return i + 1
		}
	}
	return n + 1
}

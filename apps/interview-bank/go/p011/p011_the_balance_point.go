// 011 · The Balance Point
package p011

func BalancePoint(nums []int) int {
	total := 0
	for _, x := range nums {
		total += x
	}
	left := 0
	for i, x := range nums {
		if left == total-left-x {
			return i
		}
		left += x
	}
	return -1
}

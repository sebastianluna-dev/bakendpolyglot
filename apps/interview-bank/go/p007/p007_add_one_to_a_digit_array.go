// 007 · Add One to a Digit Array
package p007

func AddOne(digits []int) []int {
	result := append([]int(nil), digits...) // a copy, so the caller's slice is untouched
	for i := len(result) - 1; i >= 0; i-- {
		if result[i] < 9 {
			result[i]++
			return result
		}
		result[i] = 0
	}
	return append([]int{1}, result...) // every digit was 9
}

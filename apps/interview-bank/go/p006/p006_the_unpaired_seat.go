// 006 · The Unpaired Seat
package p006

func UnpairedSeat(log []int) int {
	result := 0
	for _, seat := range log {
		result ^= seat // pairs cancel: x ^ x is 0
	}
	return result
}

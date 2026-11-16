// 166 · Count the One Bits for Every Number
package p166

func OneBitCounts(n int) []int {
	bits := make([]int, n+1)
	for i := 1; i <= n; i++ {
		bits[i] = bits[i>>1] + (i & 1) // i without its last bit, plus that bit
	}
	return bits
}

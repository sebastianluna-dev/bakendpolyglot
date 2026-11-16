// 018 · Count Stretches With a Target Sum
package p018

func CountStretches(changes []int, target int) int {
	seen := map[int]int{0: 1} // prefix sum -> how many times; 0 is the empty prefix
	sum, count := 0, 0
	for _, change := range changes {
		sum += change
		count += seen[sum-target]
		seen[sum]++
	}
	return count
}

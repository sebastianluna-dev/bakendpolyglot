// 099 · Search a Sorted Matrix
package p099

func SearchSortedMatrix(shelves [][]int, target int) bool {
	rows, cols := len(shelves), 0
	if rows > 0 {
		cols = len(shelves[0])
	}
	total := rows * cols
	lo, hi := 0, total // positions 0 .. total-1, read row by row as one sorted array
	for lo < hi {
		mid := lo + (hi-lo)/2
		if shelves[mid/cols][mid%cols] >= target {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	return lo < total && shelves[lo/cols][lo%cols] == target
}

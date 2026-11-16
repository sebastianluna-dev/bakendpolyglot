// 048 · Remove Duplicates From a Sorted Array
package p048

func RemoveDuplicates(ids []int) int {
	if len(ids) == 0 {
		return 0
	}
	write := 1 // ids[:write] holds the distinct ids kept so far
	for read := 1; read < len(ids); read++ {
		if ids[read] != ids[write-1] {
			ids[write] = ids[read]
			write++
		}
	}
	return write
}

// 186 · Insert a New Interval
package p186

func InsertInterval(intervals [][]int, newInterval []int) [][]int {
	result := [][]int{}
	start, end := newInterval[0], newInterval[1]
	i := 0
	for ; i < len(intervals) && intervals[i][1] < start; i++ {
		result = append(result, intervals[i])
	}
	for ; i < len(intervals) && intervals[i][0] <= end; i++ {
		start = min(start, intervals[i][0])
		end = max(end, intervals[i][1])
	}
	result = append(result, []int{start, end})
	return append(result, intervals[i:]...)
}

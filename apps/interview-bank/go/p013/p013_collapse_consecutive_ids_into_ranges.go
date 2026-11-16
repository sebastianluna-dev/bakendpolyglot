// 013 · Collapse Consecutive IDs Into Ranges
package p013

import "strconv"

func CollapseRanges(ids []int) []string {
	ranges := []string{}
	for i := 0; i < len(ids); i++ {
		start := ids[i]
		for i+1 < len(ids) && ids[i+1] == ids[i]+1 {
			i++
		}
		if start == ids[i] {
			ranges = append(ranges, strconv.Itoa(start))
		} else {
			ranges = append(ranges, strconv.Itoa(start)+"-"+strconv.Itoa(ids[i]))
		}
	}
	return ranges
}

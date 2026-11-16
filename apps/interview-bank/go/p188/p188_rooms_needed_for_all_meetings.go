// 188 · Rooms Needed for All Meetings
package p188

import "slices"

func RoomsNeeded(meetings [][]int) int {
	starts := make([]int, len(meetings))
	ends := make([]int, len(meetings))
	for i, m := range meetings {
		starts[i], ends[i] = m[0], m[1]
	}
	slices.Sort(starts)
	slices.Sort(ends)
	rooms, j := 0, 0 // ends[j] is the earliest end that has not freed a room yet
	for _, start := range starts {
		if start >= ends[j] {
			j++ // a meeting is over: reuse its room
		} else {
			rooms++
		}
	}
	return rooms
}

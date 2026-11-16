// 017 · Longest Run of Consecutive IDs
package p017

func LongestRun(ids []int) int {
	present := map[int]bool{}
	for _, id := range ids {
		present[id] = true
	}
	best := 0
	for id := range present {
		if present[id-1] {
			continue // not the start of a run
		}
		length := 1
		for present[id+length] {
			length++
		}
		best = max(best, length)
	}
	return best
}

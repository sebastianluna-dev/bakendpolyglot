// 012 · Shared Members of Two Lists
package p012

func SharedMembers(first, second []int) []int {
	pending := map[int]bool{}
	for _, id := range second {
		pending[id] = true
	}
	shared := []int{}
	for _, id := range first {
		if pending[id] {
			delete(pending, id) // removed, so a repeat in first is skipped
			shared = append(shared, id)
		}
	}
	return shared
}

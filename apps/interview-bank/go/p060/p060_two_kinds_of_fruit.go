// 060 · Two Kinds of Fruit
package p060

func LongestTwoKinds(crates []int) int {
	inWindow := map[int]int{} // kind -> crates of it in the window
	left, best := 0, 0
	for right, kind := range crates {
		inWindow[kind]++
		for len(inWindow) > 2 {
			out := crates[left]
			inWindow[out]--
			if inWindow[out] == 0 {
				delete(inWindow, out) // a kind with no crates left must not count
			}
			left++
		}
		best = max(best, right-left+1)
	}
	return best
}

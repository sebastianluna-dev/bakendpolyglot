// 164 · Rob Houses Without Tripping the Alarm
package p164

func MaxHaul(cash []int) int {
	skipPrev, best := 0, 0 // best totals up to two houses back and up to here
	for _, c := range cash {
		skipPrev, best = best, max(best, skipPrev+c)
	}
	return best
}

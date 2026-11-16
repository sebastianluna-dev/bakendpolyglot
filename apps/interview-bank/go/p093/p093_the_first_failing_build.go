// 093 · The First Failing Build
package p093

func FirstFailingBuild(n int, isBad func(build int) bool) int {
	lo, hi := 1, n+1 // n + 1 stands for "no build fails"
	for lo < hi {
		mid := lo + (hi-lo)/2
		if isBad(mid) {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	if lo <= n {
		return lo
	}
	return -1
}

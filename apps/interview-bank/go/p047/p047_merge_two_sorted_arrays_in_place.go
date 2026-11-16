// 047 · Merge Two Sorted Arrays in Place
package p047

func MergeInPlace(a []int, m int, b []int) {
	i, j := m-1, len(b)-1
	for k := m + len(b) - 1; j >= 0; k-- { // k: next slot to fill, from the back
		if i >= 0 && a[i] > b[j] {
			a[k] = a[i]
			i--
		} else {
			a[k] = b[j]
			j--
		}
	}
}

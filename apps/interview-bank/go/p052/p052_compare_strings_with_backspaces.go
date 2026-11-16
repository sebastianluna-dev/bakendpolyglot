// 052 · Compare Strings With Backspaces
package p052

func SameAfterBackspaces(a, b string) bool {
	i, j := len(a)-1, len(b)-1
	for i >= 0 || j >= 0 {
		i = lastKept(a, i)
		j = lastKept(b, j)
		if i < 0 || j < 0 {
			return i < 0 && j < 0
		}
		if a[i] != b[j] {
			return false
		}
		i--
		j--
	}
	return true
}

// lastKept returns the index of the last byte at or before i that no later '#' deletes, or -1.
func lastKept(s string, i int) int {
	skip := 0
	for i >= 0 {
		if s[i] == '#' {
			skip++
		} else if skip > 0 {
			skip--
		} else {
			break
		}
		i--
	}
	return i
}

// 157 · Every Balanced Bracket String
package p157

func BalancedBrackets(n int) []string {
	result := []string{}
	path := make([]byte, 0, 2*n)
	var walk func(opened, closed int)
	walk = func(opened, closed int) {
		if closed == n {
			result = append(result, string(path))
			return
		}
		if opened < n {
			path = append(path, '(')
			walk(opened+1, closed)
			path = path[:len(path)-1]
		}
		if closed < opened {
			path = append(path, ')')
			walk(opened, closed+1)
			path = path[:len(path)-1]
		}
	}
	walk(0, 0)
	return result
}

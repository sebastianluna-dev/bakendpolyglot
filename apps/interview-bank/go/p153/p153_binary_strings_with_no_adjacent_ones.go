// 153 · Binary Strings With No Adjacent Ones
package p153

func NoAdjacentOnes(n int) []string {
	result := []string{}
	path := make([]byte, 0, n)
	var walk func()
	walk = func() {
		if len(path) == n {
			result = append(result, string(path)) // string(path) copies the bytes
			return
		}
		path = append(path, '0')
		walk()
		path = path[:len(path)-1]
		if len(path) == 0 || path[len(path)-1] == '0' {
			path = append(path, '1')
			walk()
			path = path[:len(path)-1]
		}
	}
	walk()
	return result
}

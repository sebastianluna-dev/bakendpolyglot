// 159 · Phone Keypad Words
package p159

var keys = [10]string{"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"}

func KeypadWords(digits string) []string {
	result := []string{}
	if len(digits) == 0 {
		return result
	}
	path := make([]byte, 0, len(digits))
	var walk func(k int)
	walk = func(k int) {
		if k == len(digits) {
			result = append(result, string(path))
			return
		}
		letters := keys[digits[k]-'0']
		for i := 0; i < len(letters); i++ {
			path = append(path, letters[i])
			walk(k + 1)
			path = path[:len(path)-1]
		}
	}
	walk(0)
	return result
}

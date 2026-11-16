// 080 · A Calculator With Parentheses
package p080

func Calculate(s string) int {
	result, sign, number := 0, 1, 0
	stack := []int{} // for each open bracket: the result before it, then the sign in front of it
	for i := 0; i < len(s); i++ {
		switch ch := s[i]; {
		case ch >= '0' && ch <= '9':
			number = number*10 + int(ch-'0')
		case ch == '+' || ch == '-':
			result += sign * number
			number = 0
			sign = 1
			if ch == '-' {
				sign = -1
			}
		case ch == '(':
			stack = append(stack, result, sign)
			result, sign = 0, 1
		case ch == ')':
			result += sign * number
			number = 0
			outerResult, outerSign := stack[len(stack)-2], stack[len(stack)-1]
			stack = stack[:len(stack)-2]
			result = outerResult + outerSign*result
		}
	}
	return result + sign*number
}

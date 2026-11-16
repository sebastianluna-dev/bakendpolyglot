// 072 · Evaluate Postfix Expressions
package p072

import "strconv"

func EvaluatePostfix(tokens []string) int {
	stack := []int{}
	for _, token := range tokens {
		switch token {
		case "+", "-", "*", "/":
			a, b := stack[len(stack)-2], stack[len(stack)-1]
			stack = stack[:len(stack)-2]
			var v int
			switch token {
			case "+":
				v = a + b
			case "-":
				v = a - b
			case "*":
				v = a * b
			default:
				v = a / b
			}
			stack = append(stack, v)
		default:
			n, _ := strconv.Atoi(token) // the input is valid, so the error is always nil
			stack = append(stack, n)
		}
	}
	return stack[len(stack)-1]
}

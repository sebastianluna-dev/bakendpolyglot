// 069 · Ledger With Undo and Double
package p069

import "strconv"

func LedgerTotal(commands []string) int {
	amounts := []int{}
	for _, cmd := range commands {
		n := len(amounts)
		switch cmd {
		case "U":
			amounts = amounts[:n-1]
		case "D":
			amounts = append(amounts, 2*amounts[n-1])
		case "+":
			amounts = append(amounts, amounts[n-1]+amounts[n-2])
		default:
			x, _ := strconv.Atoi(cmd) // the statement guarantees a valid integer
			amounts = append(amounts, x)
		}
	}
	total := 0
	for _, x := range amounts {
		total += x
	}
	return total
}

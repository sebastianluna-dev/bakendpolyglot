// 004 · The Missing Ticket
package p004

func MissingTicket(scanned []int) int {
	n := len(scanned) + 1
	total := n * (n + 1) / 2 // int is 64 bits on the usual platforms
	for _, t := range scanned {
		total -= t
	}
	return total
}

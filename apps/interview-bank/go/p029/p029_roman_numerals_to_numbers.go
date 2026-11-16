// 029 · Roman Numerals to Numbers
package p029

var values = map[byte]int{'I': 1, 'V': 5, 'X': 10, 'L': 50, 'C': 100, 'D': 500, 'M': 1000}

func RomanToNumber(roman string) int {
	total := 0
	for i := 0; i < len(roman); i++ {
		value := values[roman[i]]
		next := 0
		if i+1 < len(roman) {
			next = values[roman[i+1]]
		}
		// a smaller symbol before a larger one is subtracted (IV = 5 - 1)
		if value < next {
			total -= value
		} else {
			total += value
		}
	}
	return total
}

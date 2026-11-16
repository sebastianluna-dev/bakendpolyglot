// 173 · Ways to Decode a Digit String
package p173

func CountDecodings(digits string) int {
	twoBack := 0 // decodings of the first i - 2 digits
	oneBack := 1 // decodings of the first i - 1 digits; the empty prefix has one
	for i := 1; i <= len(digits); i++ {
		ways := 0
		if digits[i-1] != '0' {
			ways = oneBack
		}
		if i >= 2 {
			two := int(digits[i-2]-'0')*10 + int(digits[i-1]-'0')
			if two >= 10 && two <= 26 { // at least 10: "06" is not a letter
				ways += twoBack
			}
		}
		twoBack, oneBack = oneBack, ways
	}
	return oneBack
}

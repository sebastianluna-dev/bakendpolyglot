// 042 · Multiply Numbers Written as Strings
package p042

func MultiplyStrings(a, b string) string {
	digits := make([]int, len(a)+len(b))
	for i := len(a) - 1; i >= 0; i-- {
		for j := len(b) - 1; j >= 0; j-- {
			sum := int(a[i]-'0')*int(b[j]-'0') + digits[i+j+1]
			digits[i+j+1] = sum % 10
			digits[i+j] += sum / 10 // may pass 9 for now; fixed when its own turn comes
		}
	}
	start := 0
	for start < len(digits)-1 && digits[start] == 0 {
		start++
	}
	result := make([]byte, 0, len(digits)-start)
	for _, d := range digits[start:] {
		result = append(result, byte('0'+d))
	}
	return string(result)
}

// 035 · Shift Cipher
package p035

func ShiftCipher(text string, shift int) string {
	k := (shift%26 + 26) % 26 // % keeps the sign of shift, so -1 must become 25
	chars := []byte(text)
	for i, ch := range chars {
		if ch >= 'a' && ch <= 'z' {
			chars[i] = 'a' + byte((int(ch-'a')+k)%26)
		} else if ch >= 'A' && ch <= 'Z' {
			chars[i] = 'A' + byte((int(ch-'A')+k)%26)
		}
	}
	return string(chars)
}

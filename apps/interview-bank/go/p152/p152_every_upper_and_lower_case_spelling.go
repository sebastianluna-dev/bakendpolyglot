// 152 · Every Upper- and Lower-Case Spelling
package p152

import "unicode"

func AllSpellings(code string) []string {
	chars := []byte(code)
	out := []string{}
	var spell func(i int)
	spell = func(i int) {
		if i == len(chars) {
			out = append(out, string(chars))
			return
		}
		c := rune(code[i])
		if !unicode.IsLetter(c) { // a digit: one choice
			spell(i + 1)
			return
		}
		chars[i] = byte(unicode.ToUpper(c)) // upper case first keeps the output sorted
		spell(i + 1)
		chars[i] = byte(unicode.ToLower(c))
		spell(i + 1)
	}
	spell(0)
	return out
}

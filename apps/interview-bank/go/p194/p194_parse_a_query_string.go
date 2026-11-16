// 194 · Parse a Query String
package p194

import "strings"

func ParseQuery(query string) map[string][]string {
	params := map[string][]string{}
	for _, piece := range strings.Split(query, "&") {
		rawKey, rawValue, _ := strings.Cut(piece, "=") // split at the first = only
		key := decode(rawKey)
		if key == "" {
			continue
		}
		params[key] = append(params[key], decode(rawValue))
	}
	return params
}

func decode(s string) string {
	var out []byte
	for i := 0; i < len(s); i++ {
		c := s[i]
		switch {
		case c == '+':
			out = append(out, ' ')
		case c == '%' && i+2 < len(s) && hexValue(s[i+1]) >= 0 && hexValue(s[i+2]) >= 0:
			out = append(out, byte(hexValue(s[i+1])*16+hexValue(s[i+2])))
			i += 2
		default:
			out = append(out, c) // a malformed % stays as it is
		}
	}
	return string(out) // a Go string is bytes, so several %XX escapes make one UTF-8 character
}

func hexValue(c byte) int {
	switch {
	case c >= '0' && c <= '9':
		return int(c - '0')
	case c >= 'a' && c <= 'f':
		return int(c-'a') + 10
	case c >= 'A' && c <= 'F':
		return int(c-'A') + 10
	}
	return -1
}

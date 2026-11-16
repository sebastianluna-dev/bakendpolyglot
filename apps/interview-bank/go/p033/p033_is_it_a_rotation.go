// 033 · Is It a Rotation?
package p033

import "strings"

func IsRotation(a, b string) bool {
	return len(a) == len(b) && strings.Contains(a+a, b)
}

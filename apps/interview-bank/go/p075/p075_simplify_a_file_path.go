// 075 · Simplify a File Path
package p075

import "strings"

func SimplifyPath(path string) string {
	names := []string{}
	for _, part := range strings.Split(path, "/") {
		switch part {
		case "..":
			if len(names) > 0 {
				names = names[:len(names)-1]
			}
		case "", ".":
		default:
			names = append(names, part)
		}
	}
	return "/" + strings.Join(names, "/")
}

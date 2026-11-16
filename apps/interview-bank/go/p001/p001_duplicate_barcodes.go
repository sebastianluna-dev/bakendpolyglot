// 001 · Duplicate Barcodes
package p001

func HasDuplicate(barcodes []string) bool {
	seen := map[string]struct{}{}
	for _, code := range barcodes {
		if _, ok := seen[code]; ok {
			return true
		}
		seen[code] = struct{}{}
	}
	return false
}

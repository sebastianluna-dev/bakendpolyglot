// 183 · Make Change at the Box Office
package p183

func CanMakeChange(bills []int) bool {
	fives, tens := 0, 0
	for _, bill := range bills {
		switch {
		case bill == 5:
			fives++
		case bill == 10:
			if fives == 0 {
				return false
			}
			fives--
			tens++
		case tens > 0 && fives > 0:
			// a 10 can only ever change a 20, so spend it before the fives
			tens--
			fives--
		case fives >= 3:
			fives -= 3
		default:
			return false
		}
	}
	return true
}

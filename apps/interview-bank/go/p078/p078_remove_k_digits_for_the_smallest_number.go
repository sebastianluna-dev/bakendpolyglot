// 078 · Remove k Digits for the Smallest Number
package p078

func RemoveKDigits(num string, k int) string {
	kept := []byte{} // used as a stack of digits
	for i := 0; i < len(num); i++ {
		d := num[i]
		for k > 0 && len(kept) > 0 && kept[len(kept)-1] > d {
			kept = kept[:len(kept)-1]
			k--
		}
		kept = append(kept, d)
	}
	kept = kept[:len(kept)-k] // digits never fell, so drop the rest from the end
	start := 0
	for start < len(kept)-1 && kept[start] == '0' {
		start++
	}
	if len(kept) == 0 {
		return "0"
	}
	return string(kept[start:])
}

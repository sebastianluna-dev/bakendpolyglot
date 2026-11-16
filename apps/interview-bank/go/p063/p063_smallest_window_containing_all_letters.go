// 063 · Smallest Window Containing All Letters
package p063

func SmallestWindow(text, letters string) string {
	var need [128]int // copies still wanted, by character code
	for i := 0; i < len(letters); i++ {
		need[letters[i]]++
	}
	missing, bestStart, bestLength, left := len(letters), 0, len(text)+1, 0
	for right := 0; right < len(text); right++ {
		c := text[right]
		if need[c] > 0 {
			missing--
		}
		need[c]--
		for missing == 0 {
			if right-left+1 < bestLength {
				bestStart, bestLength = left, right-left+1
			}
			d := text[left]
			need[d]++
			if need[d] > 0 {
				missing++ // a wanted copy just left the window
			}
			left++
		}
	}
	if bestLength > len(text) {
		return ""
	}
	return text[bestStart : bestStart+bestLength]
}

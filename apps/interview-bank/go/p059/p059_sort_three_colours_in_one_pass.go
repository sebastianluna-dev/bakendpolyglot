// 059 · Sort Three Colours in One Pass
package p059

func SortColours(stickers []int) {
	low, mid, high := 0, 0, len(stickers)-1 // [0, low) is 0, [low, mid) is 1, (high, end] is 2
	for mid <= high {
		switch stickers[mid] {
		case 0:
			stickers[low], stickers[mid] = stickers[mid], stickers[low]
			low++
			mid++
		case 1:
			mid++
		default:
			stickers[mid], stickers[high] = stickers[high], stickers[mid]
			high-- // mid stays: the value swapped in has not been looked at yet
		}
	}
}

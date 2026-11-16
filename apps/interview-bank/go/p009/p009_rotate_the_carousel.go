// 009 · Rotate the Carousel
package p009

import "slices"

func RotateCarousel(slides []int, k int) {
	n := len(slides)
	if n == 0 {
		return
	}
	k %= n
	slices.Reverse(slides)
	slices.Reverse(slides[:k]) // subslices share the array, so this reverses in place
	slices.Reverse(slides[k:])
}

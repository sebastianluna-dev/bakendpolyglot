// 022 · Rotate a Square Image
package p022

import "slices"

func RotateImage(image [][]int) {
	n := len(image)
	for i := 0; i < n; i++ {
		for j := i + 1; j < n; j++ {
			image[i][j], image[j][i] = image[j][i], image[i][j]
		}
	}
	for _, row := range image {
		slices.Reverse(row)
	}
}

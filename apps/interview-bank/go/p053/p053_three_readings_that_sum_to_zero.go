// 053 · Three Readings That Sum to Zero
package p053

import "slices"

func ThreeToZero(readings []int) [][]int {
	s := slices.Clone(readings)
	slices.Sort(s)
	result := [][]int{}
	for i := 0; i < len(s)-2; i++ {
		if i > 0 && s[i] == s[i-1] {
			continue // same first value, same triplets
		}
		lo, hi := i+1, len(s)-1
		for lo < hi {
			sum := s[i] + s[lo] + s[hi]
			if sum < 0 {
				lo++
			} else if sum > 0 {
				hi--
			} else {
				result = append(result, []int{s[i], s[lo], s[hi]})
				lo++
				hi--
				for lo < hi && s[lo] == s[lo-1] {
					lo++
				}
			}
		}
	}
	return result
}

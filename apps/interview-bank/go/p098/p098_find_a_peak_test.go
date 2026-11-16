package p098

import "testing"

func isPeak(a []int, i int) bool {
	return i >= 0 && i < len(a) && (i == 0 || a[i-1] < a[i]) && (i == len(a)-1 || a[i+1] < a[i])
}

func TestFindPeak(t *testing.T) {
	routes := [][]int{
		{120, 135, 150, 140, 160, 155},
		{10, 20, 30},
		{5},
		{3, 1},
		{1, 3},
		{5, 4, 3, 2, 1},
		{1, 3, 2, 4, 3, 5, 4},
		{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9},
	}
	for _, route := range routes {
		if got := FindPeak(route); !isPeak(route, got) {
			t.Errorf("FindPeak(%v) = %d, which is not a peak", route, got)
		}
	}
}

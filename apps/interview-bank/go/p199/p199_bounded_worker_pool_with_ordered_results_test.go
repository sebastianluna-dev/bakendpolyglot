package p199

import (
	"slices"
	"sync"
	"testing"
	"time"
)

func measure(durations []int, limit int) ([]int, int) {
	var mu sync.Mutex
	running, peak := 0, 0
	results := MapLimit(durations, limit, func(ms int) int {
		mu.Lock()
		running++
		peak = max(peak, running)
		mu.Unlock()
		time.Sleep(time.Duration(ms) * time.Millisecond)
		mu.Lock()
		running--
		mu.Unlock()
		return ms * 10
	})
	return results, peak
}

func TestMapLimit(t *testing.T) {
	cases := []struct {
		durations []int
		limit     int
		want      []int
		wantPeak  int
	}{
		{[]int{40, 20, 30, 20, 25, 20, 30, 20}, 3, []int{400, 200, 300, 200, 250, 200, 300, 200}, 3},
		{[]int{20, 20, 20}, 1, []int{200, 200, 200}, 1},
		{[]int{30, 20}, 10, []int{300, 200}, 2},
		{[]int{}, 4, []int{}, 0},
	}
	for _, c := range cases {
		got, peak := measure(c.durations, c.limit)
		if !slices.Equal(got, c.want) || peak != c.wantPeak {
			t.Errorf("MapLimit(%v, %d) = %v with peak %d, want %v with peak %d", c.durations, c.limit, got, peak, c.want, c.wantPeak)
		}
	}
}

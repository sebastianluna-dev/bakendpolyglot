package p149

import (
	"math"
	"slices"
	"testing"
)

func TestRunningMedian(t *testing.T) {
	m := NewRunningMedian()
	neg := NewRunningMedian()
	steps := []struct {
		name string
		add  func()
		got  func() float64
		want float64
	}{
		{"AddNum(5)", func() { m.AddNum(5) }, m.FindMedian, 5},
		{"AddNum(15)", func() { m.AddNum(15) }, m.FindMedian, 10},
		{"AddNum(1)", func() { m.AddNum(1) }, m.FindMedian, 5},
		{"AddNum(3)", func() { m.AddNum(3) }, m.FindMedian, 4},
		{"AddNum(-2)", func() { neg.AddNum(-2) }, neg.FindMedian, -2},
		{"AddNum(-2) again", func() { neg.AddNum(-2) }, neg.FindMedian, -2},
		{"AddNum(7)", func() { neg.AddNum(7) }, neg.FindMedian, -2},
		{"AddNum(8)", func() { neg.AddNum(8) }, neg.FindMedian, 2.5},
	}
	for _, s := range steps {
		s.add()
		if got := s.got(); got != s.want {
			t.Errorf("%s: FindMedian() = %v, want %v", s.name, got, s.want)
		}
	}

	down := NewRunningMedian()
	medians := []float64{}
	for _, x := range []int{9, 8, 7, 6, 5} {
		down.AddNum(x)
		medians = append(medians, down.FindMedian())
	}
	if want := []float64{9, 8.5, 8, 7.5, 7}; !slices.Equal(medians, want) {
		t.Errorf("medians of 9, 8, 7, 6, 5 = %v, want %v", medians, want)
	}

	big := NewRunningMedian()
	big.AddNum(math.MaxInt32)
	big.AddNum(math.MaxInt32)
	if got := big.FindMedian(); got != math.MaxInt32 {
		t.Errorf("two values of MaxInt32: FindMedian() = %v, want %v", got, math.MaxInt32)
	}
	big.AddNum(math.MinInt32)
	big.AddNum(math.MinInt32)
	if got := big.FindMedian(); got != -0.5 {
		t.Errorf("after adding two of MinInt32: FindMedian() = %v, want -0.5", got)
	}
}

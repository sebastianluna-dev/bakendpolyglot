package p023

import (
	"slices"
	"testing"
)

func picks(s *RandomSet, count int) []int {
	out := make([]int, count)
	for k := range out {
		out[k] = s.Pick()
	}
	slices.Sort(out)
	return out
}

func TestRandomSet(t *testing.T) {
	calls := 0
	s := NewRandomSet(func(n int) int {
		calls++
		return (calls - 1) % n
	})
	steps := []struct {
		name string
		got  bool
		want bool
	}{
		{"Add(12)", s.Add(12), true},
		{"Add(7)", s.Add(7), true},
		{"Add(12) again", s.Add(12), false},
		{"Add(30)", s.Add(30), true},
		{"Remove(7)", s.Remove(7), true},
		{"Remove(7) again", s.Remove(7), false},
		{"Remove(99)", s.Remove(99), false},
		{"two picks", slices.Equal(picks(s, 2), []int{12, 30}), true},
		{"Add(5)", s.Add(5), true},
		{"three picks", slices.Equal(picks(s, 3), []int{5, 12, 30}), true},
		{"Remove(30)", s.Remove(30), true},
		{"Remove(5), the last item", s.Remove(5), true},
		{"Add(5) after removing it", s.Add(5), true},
		{"Remove(12)", s.Remove(12), true},
		{"Pick() with one item", s.Pick() == 5, true},
	}
	for _, st := range steps {
		if st.got != st.want {
			t.Errorf("%s = %v, want %v", st.name, st.got, st.want)
		}
	}
}

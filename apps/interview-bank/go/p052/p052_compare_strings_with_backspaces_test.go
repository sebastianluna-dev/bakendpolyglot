package p052

import "testing"

func TestSameAfterBackspaces(t *testing.T) {
	cases := []struct {
		a, b string
		want bool
	}{
		{"A1#2", "A2", true},
		{"B7##C", "C#B", false},
		{"##", "", true},
		{"ab##", "c#d#", true},
		{"x#y", "y", true},
		{"abc", "ab", false},
		{"a", "ba", false},
		{"a#b#c", "#c", true},
	}
	for _, c := range cases {
		if got := SameAfterBackspaces(c.a, c.b); got != c.want {
			t.Errorf("SameAfterBackspaces(%q, %q) = %v, want %v", c.a, c.b, got, c.want)
		}
	}
}

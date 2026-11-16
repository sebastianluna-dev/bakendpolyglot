package p146

import "testing"

func TestRearrange(t *testing.T) {
	cases := []struct {
		s    string
		want string
	}{
		{"aab", "aba"},
		{"aaab", ""},
		{"vvvlo", "vlvov"},
		{"aabbcc", "abcabc"},
		{"aaabbc", "ababac"},
		{"a", "a"},
		{"zz", ""},
	}
	for _, c := range cases {
		if got := Rearrange(c.s); got != c.want {
			t.Errorf("Rearrange(%q) = %q, want %q", c.s, got, c.want)
		}
	}
}

package p035

import "testing"

func TestShiftCipher(t *testing.T) {
	cases := []struct {
		text  string
		shift int
		want  string
	}{
		{"Hold seat 14!", 3, "Krog vhdw 14!"},
		{"xyz", 2, "zab"},
		{"Abc", -1, "Zab"},
		{"abc", 52, "abc"},
		{"Zz", -27, "Yy"},
		{"Krog vhdw 14!", -3, "Hold seat 14!"},
		{"", 5, ""},
	}
	for _, c := range cases {
		if got := ShiftCipher(c.text, c.shift); got != c.want {
			t.Errorf("ShiftCipher(%q, %d) = %q, want %q", c.text, c.shift, got, c.want)
		}
	}
}

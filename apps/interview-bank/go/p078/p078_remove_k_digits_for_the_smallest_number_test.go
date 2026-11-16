package p078

import "testing"

func TestRemoveKDigits(t *testing.T) {
	cases := []struct {
		num  string
		k    int
		want string
	}{
		{"4172", 2, "12"},
		{"30200", 1, "200"},
		{"9", 1, "0"},
		{"12345", 2, "123"},
		{"10", 1, "0"},
		{"1432219", 0, "1432219"},
		{"112", 1, "11"},
		{"100200", 1, "200"},
	}
	for _, c := range cases {
		if got := RemoveKDigits(c.num, c.k); got != c.want {
			t.Errorf("RemoveKDigits(%q, %d) = %q, want %q", c.num, c.k, got, c.want)
		}
	}
}

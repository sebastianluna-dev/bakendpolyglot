package p042

import "testing"

func TestMultiplyStrings(t *testing.T) {
	cases := []struct {
		a, b string
		want string
	}{
		{"123", "45", "5535"},
		{"0", "9081", "0"},
		{"999", "999", "998001"},
		{"7", "8", "56"},
		{"1", "1", "1"},
		{"123456789", "987654321", "121932631112635269"},
		{"99999999999999999999", "99999999999999999999", "9999999999999999999800000000000000000001"},
	}
	for _, c := range cases {
		if got := MultiplyStrings(c.a, c.b); got != c.want {
			t.Errorf("MultiplyStrings(%q, %q) = %q, want %q", c.a, c.b, got, c.want)
		}
	}
}

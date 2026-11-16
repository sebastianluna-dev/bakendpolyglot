package p041

import "testing"

func TestCompareVersions(t *testing.T) {
	cases := []struct {
		a, b string
		want int
	}{
		{"1.2.10", "1.2.9", 1},
		{"3.0.0", "3", 0},
		{"1.01", "1.001", 0},
		{"0.9", "1.0", -1},
		{"7.5.2.4", "7.5.3", -1},
		{"1.0.1", "1", 1},
		{"10.4", "9.12", 1},
	}
	for _, c := range cases {
		if got := CompareVersions(c.a, c.b); got != c.want {
			t.Errorf("CompareVersions(%q, %q) = %d, want %d", c.a, c.b, got, c.want)
		}
	}
}

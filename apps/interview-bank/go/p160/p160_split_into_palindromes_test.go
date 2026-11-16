package p160

import (
	"reflect"
	"slices"
	"testing"
)

func TestPalindromeSplits(t *testing.T) {
	cases := []struct {
		s    string
		want [][]string
	}{
		{"aab", [][]string{{"a", "a", "b"}, {"aa", "b"}}},
		{"noon", [][]string{{"n", "o", "o", "n"}, {"n", "oo", "n"}, {"noon"}}},
		{"a", [][]string{{"a"}}},
		{"ab", [][]string{{"a", "b"}}},
		{"aaa", [][]string{{"a", "a", "a"}, {"a", "aa"}, {"aa", "a"}, {"aaa"}}},
	}
	for _, c := range cases {
		if got := PalindromeSplits(c.s); !reflect.DeepEqual(got, c.want) {
			t.Errorf("PalindromeSplits(%q) = %v, want %v", c.s, got, c.want)
		}
	}
	all := PalindromeSplits("racecar")
	if last := all[len(all)-1]; !slices.Equal(last, []string{"racecar"}) {
		t.Errorf("last split of \"racecar\" = %v, want [racecar]", last)
	}
}

package p040

import (
	"slices"
	"testing"
)

func TestPackAndUnpack(t *testing.T) {
	cases := []struct {
		words  []string
		packed string
	}{
		{[]string{"truck 7", "", "door#2"}, "7#truck 70#6#door#2"},
		{[]string{"parcel-12345"}, "12#parcel-12345"},
		{[]string{}, ""},
		{[]string{""}, "0#"},
		{[]string{"#", "12#", "a b", "3#x"}, "1##3#12#3#a b3#3#x"},
	}
	for _, c := range cases {
		if got := Pack(c.words); got != c.packed {
			t.Errorf("Pack(%q) = %q, want %q", c.words, got, c.packed)
		}
		if got := Unpack(c.packed); !slices.Equal(got, c.words) {
			t.Errorf("Unpack(%q) = %q, want %q", c.packed, got, c.words)
		}
	}
}

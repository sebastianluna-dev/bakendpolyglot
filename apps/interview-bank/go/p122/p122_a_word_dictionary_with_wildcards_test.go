package p122

import "testing"

func TestWordDictionary(t *testing.T) {
	expect := func(d *WordDictionary, pattern string, want bool) {
		t.Helper()
		if got := d.Search(pattern); got != want {
			t.Errorf("Search(%q) = %t, want %t", pattern, got, want)
		}
	}

	d := NewWordDictionary()
	d.AddWord("loan")
	d.AddWord("lean")
	expect(d, "l.an", true)
	expect(d, "lo", false)
	expect(d, "l...", true)
	d.AddWord("load")
	expect(d, "loan", true)
	expect(d, "lo.d", true)
	expect(d, "le.d", false)
	expect(d, "loans", false)
	expect(d, "....", true)
	expect(d, "...", false)
	expect(d, "x...", false)

	e := NewWordDictionary()
	expect(e, ".", false)
	e.AddWord("a")
	expect(e, ".", true)
	expect(e, "a", true)
	expect(e, "b", false)
	e.AddWord("a")
	expect(e, "a", true)
}

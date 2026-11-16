package p200

import "testing"

var keys = []string{"book:1", "book:2", "book:3", "book:4", "book:5", "book:6", "book:7", "book:37"}

func owners(r *HashRing) string {
	out := ""
	for _, k := range keys {
		if node := r.NodeFor(k); node != "" {
			out += node[len(node)-1:] // the last letter of each key's node
		}
	}
	return out
}

func TestHashes(t *testing.T) {
	cases := []struct {
		name      string
		got, want uint32
	}{
		{`Fnv1a("")`, Fnv1a(""), 0x811c9dc5},
		{`Fnv1a("a")`, Fnv1a("a"), 0xe40c292c},
		{`Fnv1a("foobar")`, Fnv1a("foobar"), 0xbf9cf968},
		{`Mix32(0)`, Mix32(0), 0},
		{`RingHash("cache-a#0")`, RingHash("cache-a#0"), 0x6901e554},
		{`RingHash("book:37")`, RingHash("book:37"), 0xfcfa1e45},
	}
	for _, c := range cases {
		if c.got != c.want {
			t.Errorf("%s = %#x, want %#x", c.name, c.got, c.want)
		}
	}
}

func TestHashRing(t *testing.T) {
	expect := func(name, got, want string) {
		t.Helper()
		if got != want {
			t.Errorf("%s = %q, want %q", name, got, want)
		}
	}

	r := NewHashRing(3)
	expect("NodeFor on an empty ring", r.NodeFor("book:1"), "")
	for _, name := range []string{"cache-a", "cache-b", "cache-c"} {
		r.AddNode(name)
	}
	expect("owners with a, b, c", owners(r), "babccccc")
	r.AddNode("cache-a")
	r.RemoveNode("cache-z")
	expect("owners after no-op calls", owners(r), "babccccc")
	r.RemoveNode("cache-b")
	expect("owners without b", owners(r), "aacccccc") // only cache-b's keys moved
	r.AddNode("cache-b")
	expect("owners with b back", owners(r), "babccccc")
	r.AddNode("cache-d")
	expect("owners with d added", owners(r), "babdccdc") // keys move only to the new node
	for _, name := range []string{"cache-a", "cache-b", "cache-c", "cache-d"} {
		r.RemoveNode(name)
	}
	expect("NodeFor after removing every node", r.NodeFor("book:1"), "")
}

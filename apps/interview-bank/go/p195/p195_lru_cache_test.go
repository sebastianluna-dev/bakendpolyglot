package p195

import "testing"

func TestLruCache(t *testing.T) {
	expect := func(name string, got, want int) {
		t.Helper()
		if got != want {
			t.Errorf("%s = %d, want %d", name, got, want)
		}
	}

	c := NewLruCache(2)
	c.Put(1, 10)
	c.Put(2, 20)
	expect("Get(1)", c.Get(1), 10)
	c.Put(3, 30) // evicts 2
	expect("Get(2) after eviction", c.Get(2), -1)
	expect("Get(3)", c.Get(3), 30)
	c.Put(1, 11) // an update counts as a use
	c.Put(4, 40) // evicts 3
	expect("Get(3) after eviction", c.Get(3), -1)
	expect("Get(1) after update", c.Get(1), 11)
	expect("Get(4)", c.Get(4), 40)

	one := NewLruCache(1)
	one.Put(5, 50)
	expect("Get(5)", one.Get(5), 50)
	one.Put(6, 60)
	expect("Get(5) after eviction", one.Get(5), -1)
	expect("Get(6)", one.Get(6), 60)
	expect("Get(7), never stored", one.Get(7), -1)
	one.Put(6, 61)
	expect("Get(6) after update", one.Get(6), 61)
}

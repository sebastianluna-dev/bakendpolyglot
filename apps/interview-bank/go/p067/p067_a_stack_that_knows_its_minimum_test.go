package p067

import "testing"

func TestMinStack(t *testing.T) {
	expect := func(name string, got, want int) {
		t.Helper()
		if got != want {
			t.Errorf("%s = %d, want %d", name, got, want)
		}
	}

	s := NewMinStack()
	s.Push(5)
	s.Push(3)
	s.Push(7)
	expect("Min()", s.Min(), 3)
	expect("Top()", s.Top(), 7)
	expect("Pop()", s.Pop(), 7)
	expect("Min() after popping 7", s.Min(), 3)
	expect("Pop()", s.Pop(), 3)
	expect("Min() after popping 3", s.Min(), 5)
	expect("Pop() the last item", s.Pop(), 5)

	s.Push(4)
	s.Push(1)
	s.Push(1)
	expect("Pop() one of two 1s", s.Pop(), 1)
	expect("Min() with a 1 left", s.Min(), 1)
	expect("Pop() the other 1", s.Pop(), 1)
	expect("Min() after both 1s", s.Min(), 4)
	s.Push(-2)
	expect("Min() after Push(-2)", s.Min(), -2)
	expect("Top() after Push(-2)", s.Top(), -2)
	expect("Pop()", s.Pop(), -2)
	expect("Top() after popping -2", s.Top(), 4)
	expect("Min() after popping -2", s.Min(), 4)
}

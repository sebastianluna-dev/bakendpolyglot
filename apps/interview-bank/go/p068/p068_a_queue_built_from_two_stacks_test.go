package p068

import "testing"

func TestTwoStackQueue(t *testing.T) {
	expect := func(name string, got, want any) {
		t.Helper()
		if got != want {
			t.Errorf("%s = %v, want %v", name, got, want)
		}
	}

	q := NewTwoStackQueue()
	expect("IsEmpty() at the start", q.IsEmpty(), true)
	q.Enqueue(1)
	q.Enqueue(2)
	expect("Peek()", q.Peek(), 1)
	expect("Dequeue()", q.Dequeue(), 1)
	q.Enqueue(3)
	expect("Dequeue() after Enqueue(3)", q.Dequeue(), 2)
	expect("Peek()", q.Peek(), 3)
	expect("IsEmpty() with one item", q.IsEmpty(), false)
	expect("Dequeue()", q.Dequeue(), 3)
	expect("IsEmpty() after the last Dequeue", q.IsEmpty(), true)
	q.Enqueue(4)
	q.Enqueue(5)
	expect("Dequeue()", q.Dequeue(), 4)
	q.Enqueue(6)
	expect("Dequeue() after Enqueue(6)", q.Dequeue(), 5)
	expect("Dequeue()", q.Dequeue(), 6)
	expect("IsEmpty() at the end", q.IsEmpty(), true)
}

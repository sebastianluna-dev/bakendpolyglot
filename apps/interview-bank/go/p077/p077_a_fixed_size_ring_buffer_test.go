package p077

import "testing"

func TestRingBuffer(t *testing.T) {
	b := NewRingBuffer(3)
	one := NewRingBuffer(1)
	steps := []struct {
		name string
		got  any
		want any
	}{
		{"IsEmpty() at start", b.IsEmpty(), true},
		{"Enqueue(5)", b.Enqueue(5), true},
		{"Enqueue(8)", b.Enqueue(8), true},
		{"Enqueue(2)", b.Enqueue(2), true},
		{"Enqueue(9) when full", b.Enqueue(9), false},
		{"IsFull()", b.IsFull(), true},
		{"Rear()", b.Rear(), 2},
		{"Dequeue()", b.Dequeue(), 5},
		{"Enqueue(9) after wrapping", b.Enqueue(9), true},
		{"Front()", b.Front(), 8},
		{"Rear() after wrapping", b.Rear(), 9},
		{"Dequeue() 8", b.Dequeue(), 8},
		{"Dequeue() 2", b.Dequeue(), 2},
		{"Dequeue() 9", b.Dequeue(), 9},
		{"Dequeue() when empty", b.Dequeue(), -1},
		{"IsEmpty() at the end", b.IsEmpty(), true},
		{"Front() when empty", b.Front(), -1},
		{"Rear() when empty", b.Rear(), -1},
		{"capacity 1: Enqueue(0)", one.Enqueue(0), true},
		{"capacity 1: IsFull()", one.IsFull(), true},
		{"capacity 1: Enqueue(1)", one.Enqueue(1), false},
		{"capacity 1: Front()", one.Front(), 0},
		{"capacity 1: Rear()", one.Rear(), 0},
		{"capacity 1: Dequeue()", one.Dequeue(), 0},
		{"capacity 1: IsEmpty()", one.IsEmpty(), true},
	}
	for _, st := range steps {
		if st.got != st.want {
			t.Errorf("%s = %v, want %v", st.name, st.got, st.want)
		}
	}
}

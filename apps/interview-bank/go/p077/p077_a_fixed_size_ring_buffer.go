// 077 · A Fixed-Size Ring Buffer
package p077

type RingBuffer struct {
	items []int
	head  int // index of the oldest value
	count int
}

func NewRingBuffer(capacity int) *RingBuffer {
	return &RingBuffer{items: make([]int, capacity)}
}

func (b *RingBuffer) Enqueue(value int) bool {
	if b.IsFull() {
		return false
	}
	b.items[(b.head+b.count)%len(b.items)] = value
	b.count++
	return true
}

func (b *RingBuffer) Dequeue() int {
	if b.IsEmpty() {
		return -1
	}
	value := b.items[b.head]
	b.head = (b.head + 1) % len(b.items)
	b.count--
	return value
}

func (b *RingBuffer) Front() int {
	if b.IsEmpty() {
		return -1
	}
	return b.items[b.head]
}

func (b *RingBuffer) Rear() int {
	if b.IsEmpty() {
		return -1
	}
	return b.items[(b.head+b.count-1)%len(b.items)]
}

func (b *RingBuffer) IsEmpty() bool { return b.count == 0 }

func (b *RingBuffer) IsFull() bool { return b.count == len(b.items) }

// 068 · A Queue Built From Two Stacks
package p068

type TwoStackQueue struct {
	inbox  []int // newest at the end
	outbox []int // oldest at the end
}

func NewTwoStackQueue() *TwoStackQueue {
	return &TwoStackQueue{}
}

func (q *TwoStackQueue) Enqueue(x int) {
	q.inbox = append(q.inbox, x)
}

func (q *TwoStackQueue) Dequeue() int {
	q.refill()
	last := len(q.outbox) - 1
	x := q.outbox[last]
	q.outbox = q.outbox[:last]
	return x
}

func (q *TwoStackQueue) Peek() int {
	q.refill()
	return q.outbox[len(q.outbox)-1]
}

func (q *TwoStackQueue) IsEmpty() bool {
	return len(q.inbox) == 0 && len(q.outbox) == 0
}

// Only when outbox is empty, or newer items would jump ahead of older ones.
func (q *TwoStackQueue) refill() {
	if len(q.outbox) > 0 {
		return
	}
	for len(q.inbox) > 0 {
		last := len(q.inbox) - 1
		q.outbox = append(q.outbox, q.inbox[last])
		q.inbox = q.inbox[:last]
	}
}

// 067 · A Stack That Knows Its Minimum
package p067

type MinStack struct {
	values []int
	mins   []int // mins[i] is the smallest of values[0..i]
}

func NewMinStack() *MinStack {
	return &MinStack{}
}

func (s *MinStack) Push(x int) {
	s.values = append(s.values, x)
	if len(s.mins) > 0 {
		x = min(x, s.mins[len(s.mins)-1])
	}
	s.mins = append(s.mins, x)
}

func (s *MinStack) Pop() int {
	last := len(s.values) - 1
	top := s.values[last]
	s.values = s.values[:last]
	s.mins = s.mins[:last]
	return top
}

func (s *MinStack) Top() int {
	return s.values[len(s.values)-1]
}

func (s *MinStack) Min() int {
	return s.mins[len(s.mins)-1]
}

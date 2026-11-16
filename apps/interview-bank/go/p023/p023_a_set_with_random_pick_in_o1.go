// 023 · A Set With Random Pick in O(1)
package p023

type RandomSet struct {
	items       []int
	position    map[int]int // value -> its index in items
	randomIndex func(n int) int
}

func NewRandomSet(randomIndex func(n int) int) *RandomSet {
	return &RandomSet{position: map[int]int{}, randomIndex: randomIndex}
}

func (s *RandomSet) Add(value int) bool {
	if _, ok := s.position[value]; ok {
		return false
	}
	s.position[value] = len(s.items)
	s.items = append(s.items, value)
	return true
}

func (s *RandomSet) Remove(value int) bool {
	i, ok := s.position[value]
	if !ok {
		return false
	}
	last := s.items[len(s.items)-1]
	s.items = s.items[:len(s.items)-1]
	if last != value {
		s.items[i] = last
		s.position[last] = i
	}
	delete(s.position, value)
	return true
}

func (s *RandomSet) Pick() int {
	return s.items[s.randomIndex(len(s.items))]
}

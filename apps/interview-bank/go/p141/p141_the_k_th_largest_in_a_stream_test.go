package p141

import "testing"

func TestKthLargest(t *testing.T) {
	expect := func(name string, got, want int) {
		t.Helper()
		if got != want {
			t.Errorf("%s = %d, want %d", name, got, want)
		}
	}

	board := NewKthLargest(3, []int{40, 75, 20, 90})
	expect("board.Add(50)", board.Add(50), 50)
	expect("board.Add(10)", board.Add(10), 50)
	expect("board.Add(80)", board.Add(80), 75)
	expect("board.Add(95)", board.Add(95), 80)

	single := NewKthLargest(1, []int{})
	expect("single.Add(-3)", single.Add(-3), -3)
	expect("single.Add(-5)", single.Add(-5), -3)
	expect("single.Add(2)", single.Add(2), 2)

	ties := NewKthLargest(2, []int{7})
	expect("ties.Add(7)", ties.Add(7), 7)
	expect("ties.Add(3)", ties.Add(3), 7)
	expect("ties.Add(9)", ties.Add(9), 7)
	expect("ties.Add(8)", ties.Add(8), 8)

	many := NewKthLargest(4, []int{5, 1, 9, 3, 7, 2})
	expect("many.Add(4)", many.Add(4), 4)
	expect("many.Add(6)", many.Add(6), 5)
	expect("many.Add(10)", many.Add(10), 6)
}

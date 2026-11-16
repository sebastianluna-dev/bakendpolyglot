package p126

import "testing"

func TestTotalImportance(t *testing.T) {
	staff := []Employee{{1, 5, []int{2, 3}}, {2, 3, []int{4}}, {3, 3, nil}, {4, 1, nil}}
	shuffled := []Employee{{3, 4, nil}, {9, 1, []int{3, 5}}, {5, 6, nil}, {8, 10, []int{9}}}
	cases := []struct {
		employees  []Employee
		employeeID int
		want       int
	}{
		{staff, 1, 12},
		{staff, 2, 4},
		{staff, 4, 1},
		{[]Employee{{7, -2, nil}}, 7, -2},
		{shuffled, 9, 11},
		{shuffled, 8, 21},
	}
	for _, c := range cases {
		if got := TotalImportance(c.employees, c.employeeID); got != c.want {
			t.Errorf("TotalImportance(%v, %d) = %d, want %d", c.employees, c.employeeID, got, c.want)
		}
	}
}

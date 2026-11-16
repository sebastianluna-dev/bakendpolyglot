// 126 · Total Importance of a Team
package p126

type Employee struct {
	ID         int
	Importance int
	Reports    []int
}

func TotalImportance(employees []Employee, employeeID int) int {
	byID := map[int]Employee{}
	for _, e := range employees {
		byID[e.ID] = e
	}
	var team func(id int) int // declared first so the closure can call itself
	team = func(id int) int {
		e := byID[id]
		total := e.Importance
		for _, r := range e.Reports {
			total += team(r)
		}
		return total
	}
	return team(employeeID)
}

// 126 · Total Importance of a Team
package p126

data class Employee(val id: Int, val importance: Int, val reports: List<Int>)

fun totalImportance(employees: List<Employee>, employeeId: Int): Int {
    val byId = employees.associateBy { it.id }
    fun team(id: Int): Int {
        val e = byId.getValue(id)
        return e.importance + e.reports.sumOf { team(it) }
    }
    return team(employeeId)
}

fun main() {
    val staff = listOf(Employee(1, 5, listOf(2, 3)), Employee(2, 3, listOf(4)), Employee(3, 3, listOf()), Employee(4, 1, listOf()))
    check(totalImportance(staff, 1) == 12)
    check(totalImportance(staff, 2) == 4)
    check(totalImportance(staff, 4) == 1)
    check(totalImportance(listOf(Employee(7, -2, listOf())), 7) == -2)
    val shuffled = listOf(Employee(3, 4, listOf()), Employee(9, 1, listOf(3, 5)), Employee(5, 6, listOf()), Employee(8, 10, listOf(9)))
    check(totalImportance(shuffled, 9) == 11)
    check(totalImportance(shuffled, 8) == 21)
    println("126 ok")
}

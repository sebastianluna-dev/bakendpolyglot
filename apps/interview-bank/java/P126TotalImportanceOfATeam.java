// 126 · Total Importance of a Team
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P126TotalImportanceOfATeam {
    record Employee(int id, int importance, List<Integer> reports) {}

    static int totalImportance(List<Employee> employees, int employeeId) {
        Map<Integer, Employee> byId = new HashMap<>();
        for (Employee e : employees) byId.put(e.id(), e);
        return team(byId, employeeId);
    }

    static int team(Map<Integer, Employee> byId, int id) {
        Employee e = byId.get(id);
        int total = e.importance();
        for (int r : e.reports()) total += team(byId, r);
        return total;
    }

    public static void main(String[] args) {
        List<Employee> staff = List.of(new Employee(1, 5, List.of(2, 3)), new Employee(2, 3, List.of(4)),
                new Employee(3, 3, List.of()), new Employee(4, 1, List.of()));
        check(totalImportance(staff, 1) == 12);
        check(totalImportance(staff, 2) == 4);
        check(totalImportance(staff, 4) == 1);
        check(totalImportance(List.of(new Employee(7, -2, List.of())), 7) == -2);
        List<Employee> shuffled = List.of(new Employee(3, 4, List.of()), new Employee(9, 1, List.of(3, 5)),
                new Employee(5, 6, List.of()), new Employee(8, 10, List.of(9)));
        check(totalImportance(shuffled, 9) == 11);
        check(totalImportance(shuffled, 8) == 21);
        System.out.println("126 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}

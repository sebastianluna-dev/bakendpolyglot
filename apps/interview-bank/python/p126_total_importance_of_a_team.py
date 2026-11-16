# 126 · Total Importance of a Team
from dataclasses import dataclass


@dataclass
class Employee:
    id: int
    importance: int
    reports: list[int]


def total_importance(employees: list[Employee], employee_id: int) -> int:
    by_id = {e.id: e for e in employees}

    def team(eid: int) -> int:
        e = by_id[eid]
        return e.importance + sum(team(r) for r in e.reports)

    return team(employee_id)


if __name__ == "__main__":
    staff = [Employee(1, 5, [2, 3]), Employee(2, 3, [4]), Employee(3, 3, []), Employee(4, 1, [])]
    assert total_importance(staff, 1) == 12
    assert total_importance(staff, 2) == 4
    assert total_importance(staff, 4) == 1
    assert total_importance([Employee(7, -2, [])], 7) == -2
    shuffled = [Employee(3, 4, []), Employee(9, 1, [3, 5]), Employee(5, 6, []), Employee(8, 10, [9])]
    assert total_importance(shuffled, 9) == 11
    assert total_importance(shuffled, 8) == 21
    print("126 ok")

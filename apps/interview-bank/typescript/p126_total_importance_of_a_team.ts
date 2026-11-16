// 126 · Total Importance of a Team
import assert from "node:assert/strict";

export interface Employee {
  id: number;
  importance: number;
  reports: number[];
}

export function totalImportance(employees: Employee[], employeeId: number): number {
  const byId = new Map<number, Employee>();
  for (const e of employees) byId.set(e.id, e);
  const team = (id: number): number => {
    const e = byId.get(id)!;
    let total = e.importance;
    for (const r of e.reports) total += team(r);
    return total;
  };
  return team(employeeId);
}

const emp = (id: number, importance: number, reports: number[]): Employee => ({ id, importance, reports });

const staff = [emp(1, 5, [2, 3]), emp(2, 3, [4]), emp(3, 3, []), emp(4, 1, [])];
assert.equal(totalImportance(staff, 1), 12);
assert.equal(totalImportance(staff, 2), 4);
assert.equal(totalImportance(staff, 4), 1);
assert.equal(totalImportance([emp(7, -2, [])], 7), -2);
const shuffled = [emp(3, 4, []), emp(9, 1, [3, 5]), emp(5, 6, []), emp(8, 10, [9])];
assert.equal(totalImportance(shuffled, 9), 11);
assert.equal(totalImportance(shuffled, 8), 21);
console.log("126 ok");

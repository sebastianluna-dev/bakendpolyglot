// 191 · Requests per Endpoint and Status From Access Logs
import assert from "node:assert/strict";

export function countRequests(lines: string[]): string[] {
  const counts = new Map<string, number>(); // "METHOD PATH STATUS" -> requests
  for (const line of lines) {
    const fields = line.split(" ");
    if (fields.length !== 5 || fields.includes("")) continue;
    const key = `${fields[1]} ${fields[2]} ${fields[3]}`;
    counts.set(key, (counts.get(key) ?? 0) + 1);
  }
  const ordered = [...counts.entries()].sort(
    (a, b) => b[1] - a[1] || (a[0] < b[0] ? -1 : a[0] > b[0] ? 1 : 0),
  );
  return ordered.map(([key, n]) => `${key} ${n}`);
}

assert.deepEqual(
  countRequests([
    "2026-03-02T09:00:01Z GET /books 200 12",
    "2026-03-02T09:00:02Z POST /loans 201 40",
    "2026-03-02T09:00:03Z GET /books 200 9",
    "2026-03-02T09:00:04Z POST /loans 409 15",
    "2026-03-02T09:00:05Z GET /books 200 11",
    "2026-03-02T09:00:06Z POST /loans 201 38",
  ]),
  ["GET /books 200 3", "POST /loans 201 2", "POST /loans 409 1"],
);
assert.deepEqual(
  countRequests([
    "2026-03-02T09:01:00Z GET /members/7 404 3",
    "",
    "garbage",
    "2026-03-02T09:01:02Z DELETE /holds/9 204 5",
    "2026-03-02T09:01:03Z GET /members/7 200 4",
  ]),
  ["DELETE /holds/9 204 1", "GET /members/7 200 1", "GET /members/7 404 1"],
);
assert.deepEqual(countRequests([]), []);
assert.deepEqual(
  countRequests([
    "2026-03-02T09:02:00Z GET  /books 200 7",
    "2026-03-02T09:02:01Z GET /books 200 7 ",
    "2026-03-02T09:02:02Z GET /books 200",
  ]),
  [],
);
assert.deepEqual(
  countRequests(["t1 GET /a 500 1", "t2 GET /b 200 1", "t3 GET /a 500 1", "t4 GET /b 200 1", "t5 GET /a 200 1"]),
  ["GET /a 500 2", "GET /b 200 2", "GET /a 200 1"],
);
console.log("191 ok");

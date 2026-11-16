// 192 · Keep the Latest Version of Each Record
import assert from "node:assert/strict";

export type Revision = { id: number; version: number; payload: string };

export function latestVersions(revisions: Revision[]): Revision[] {
  const latest = new Map<number, Revision>();
  for (const r of revisions) {
    const kept = latest.get(r.id);
    if (kept === undefined || r.version > kept.version) latest.set(r.id, r); // strictly greater: the first of equal versions stays
  }
  return [...latest.values()].sort((a, b) => a.id - b.id);
}

const R = (id: number, version: number, payload: string): Revision => ({ id, version, payload });
assert.deepEqual(
  latestVersions([R(7, 1, "created"), R(3, 2, "in transit"), R(7, 3, "delivered"), R(3, 1, "created"), R(7, 2, "in transit")]),
  [R(3, 2, "in transit"), R(7, 3, "delivered")],
);
assert.deepEqual(latestVersions([R(5, 4, "first"), R(5, 4, "replayed"), R(1, 1, "only")]), [
  R(1, 1, "only"),
  R(5, 4, "first"),
]);
assert.deepEqual(latestVersions([]), []);
assert.deepEqual(latestVersions([R(42, 9, "x")]), [R(42, 9, "x")]);
assert.deepEqual(latestVersions([R(10, 1, "a"), R(2, 1, "b"), R(10, 2, "c"), R(-4, 3, "d"), R(2, 5, "e")]), [
  R(-4, 3, "d"),
  R(2, 5, "e"),
  R(10, 2, "c"),
]);
console.log("192 ok");

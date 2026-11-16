// 193 · Cursor Pagination Over a Sorted List
import assert from "node:assert/strict";

export type Page = { items: number[]; next: number | null };

export function fetchPage(ids: number[], after: number | null, limit: number): Page {
  let start = 0;
  if (after !== null) {
    let lo = 0;
    let hi = ids.length; // looking for the first id greater than after
    while (lo < hi) {
      const mid = lo + Math.floor((hi - lo) / 2);
      if (ids[mid] > after) hi = mid;
      else lo = mid + 1;
    }
    start = lo;
  }
  const end = Math.min(start + limit, ids.length);
  const items = ids.slice(start, end);
  return { items, next: end < ids.length ? items[items.length - 1] : null };
}

const ids = [3, 8, 15, 21, 30, 42, 57];
assert.deepEqual(fetchPage(ids, null, 3), { items: [3, 8, 15], next: 15 });
assert.deepEqual(fetchPage(ids, 15, 3), { items: [21, 30, 42], next: 42 });
assert.deepEqual(fetchPage(ids, 42, 3), { items: [57], next: null });
assert.deepEqual(fetchPage(ids, 20, 3), { items: [21, 30, 42], next: 42 });
assert.deepEqual(fetchPage(ids, 57, 3), { items: [], next: null });
assert.deepEqual(fetchPage(ids, 1, 10), { items: [3, 8, 15, 21, 30, 42, 57], next: null });
assert.deepEqual(fetchPage([3, 8, 15, 21, 30, 42], 15, 3), { items: [21, 30, 42], next: null });
assert.deepEqual(fetchPage([], null, 5), { items: [], next: null });
console.log("193 ok");

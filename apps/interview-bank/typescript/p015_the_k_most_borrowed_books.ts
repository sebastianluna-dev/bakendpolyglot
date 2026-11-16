// 015 · The k Most Borrowed Books
import assert from "node:assert/strict";

export function mostBorrowed(loans: string[], k: number): string[] {
  const counts = new Map<string, number>();
  for (const title of loans) counts.set(title, (counts.get(title) ?? 0) + 1);
  const titles = [...counts.keys()];
  // localeCompare would depend on the locale; < and > compare by character code
  titles.sort((a, b) => counts.get(b)! - counts.get(a)! || (a < b ? -1 : a > b ? 1 : 0));
  return titles.slice(0, k);
}

const loans = ["dune", "emma", "dune", "ulysses", "emma", "dune", "beloved"];
assert.deepEqual(mostBorrowed(loans, 2), ["dune", "emma"]);
assert.deepEqual(mostBorrowed(["emma", "beloved", "ulysses", "beloved", "emma"], 2), ["beloved", "emma"]);
assert.deepEqual(mostBorrowed(["dune"], 3), ["dune"]);
assert.deepEqual(mostBorrowed([], 1), []);
assert.deepEqual(mostBorrowed(["c", "b", "a"], 2), ["a", "b"]);
console.log("015 ok");

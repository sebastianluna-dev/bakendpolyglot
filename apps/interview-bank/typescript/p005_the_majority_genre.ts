// 005 · The Majority Genre
import assert from "node:assert/strict";

export function majorityGenre(loans: string[]): string {
  const counts = new Map<string, number>();
  for (const genre of loans) {
    const c = (counts.get(genre) ?? 0) + 1;
    counts.set(genre, c);
    if (c * 2 > loans.length) return genre;
  }
  return "";
}

assert.equal(majorityGenre(["mystery", "poetry", "mystery", "history", "mystery"]), "mystery");
assert.equal(majorityGenre(["poetry", "history", "poetry", "history"]), "");
assert.equal(majorityGenre(["drama", "comedy", "drama"]), "drama");
assert.equal(majorityGenre(["sci-fi"]), "sci-fi");
assert.equal(majorityGenre([]), "");
console.log("005 ok");

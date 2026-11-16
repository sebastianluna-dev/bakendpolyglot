// 125 · Find the Trusted Judge
import assert from "node:assert/strict";

export function findJudge(n: number, trust: number[][]): number {
  const score = new Array<number>(n + 1).fill(0); // trusted by, minus trusts
  for (const [a, b] of trust) {
    score[a]--;
    score[b]++;
  }
  for (let person = 1; person <= n; person++) {
    if (score[person] === n - 1) return person;
  }
  return -1;
}

assert.equal(findJudge(4, [[1, 3], [2, 3], [4, 3], [4, 1]]), 3);
assert.equal(findJudge(3, [[1, 3], [2, 3], [3, 1]]), -1);
assert.equal(findJudge(1, []), 1);
assert.equal(findJudge(2, []), -1);
assert.equal(findJudge(2, [[1, 2], [2, 1]]), -1);
assert.equal(findJudge(3, [[1, 2], [2, 3]]), -1);
console.log("125 ok");

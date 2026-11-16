// 051 · Best Average Over k Days
import assert from "node:assert/strict";

export function bestAverage(balances: number[], k: number): number {
  let sum = 0;
  for (let i = 0; i < k; i++) sum += balances[i];
  let best = sum;
  for (let i = k; i < balances.length; i++) {
    sum += balances[i] - balances[i - k]; // the day entering minus the day leaving
    best = Math.max(best, sum);
  }
  return best / k;
}

const close = (got: number, want: number) => Math.abs(got - want) < 1e-9;
assert.ok(close(bestAverage([4, -2, 7, 5, -1, 6], 4), 4.25));
assert.ok(close(bestAverage([-3, -1, -4], 2), -2));
assert.ok(close(bestAverage([5], 1), 5));
assert.ok(close(bestAverage([1, 2, 3, 4], 1), 4));
assert.ok(close(bestAverage([-7, -7, -7], 3), -7));
assert.ok(close(bestAverage([1, 1, 2], 3), 4 / 3));
console.log("051 ok");

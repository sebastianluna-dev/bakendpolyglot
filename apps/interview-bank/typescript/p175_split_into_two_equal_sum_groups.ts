// 175 · Split Into Two Equal-Sum Groups
import assert from "node:assert/strict";

export function canSplitEvenly(weights: number[]): boolean {
  let total = 0;
  for (const w of weights) total += w;
  if (total % 2 !== 0) return false;
  const half = total / 2;
  const reachable = new Array<boolean>(half + 1).fill(false); // reachable[s]: some parcels seen so far weigh s
  reachable[0] = true;
  for (const w of weights) {
    for (let s = half; s >= w; s--) { // downwards, so this parcel is counted at most once
      if (reachable[s - w]) reachable[s] = true;
    }
  }
  return reachable[half];
}

assert.equal(canSplitEvenly([3, 1, 4, 2, 2]), true);
assert.equal(canSplitEvenly([2, 3, 7]), false);
assert.equal(canSplitEvenly([1]), false);
assert.equal(canSplitEvenly([5, 5]), true);
assert.equal(canSplitEvenly([1, 5]), false);
assert.equal(canSplitEvenly([1, 2, 4]), false);
assert.equal(canSplitEvenly([1, 2, 3, 4, 5, 6, 7]), true);
assert.equal(canSplitEvenly([6, 6, 6, 6, 6, 4]), false);
assert.equal(canSplitEvenly(new Array<number>(200).fill(100)), true);
console.log("175 ok");

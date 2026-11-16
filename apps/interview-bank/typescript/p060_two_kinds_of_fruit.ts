// 060 · Two Kinds of Fruit
import assert from "node:assert/strict";

export function longestTwoKinds(crates: number[]): number {
  const inWindow = new Map<number, number>(); // kind -> crates of it in the window
  let left = 0;
  let best = 0;
  for (let right = 0; right < crates.length; right++) {
    inWindow.set(crates[right], (inWindow.get(crates[right]) ?? 0) + 1);
    while (inWindow.size > 2) {
      const kind = crates[left];
      const count = inWindow.get(kind)! - 1;
      if (count === 0) inWindow.delete(kind); // a kind with no crates left must not count
      else inWindow.set(kind, count);
      left++;
    }
    best = Math.max(best, right - left + 1);
  }
  return best;
}

assert.equal(longestTwoKinds([3, 3, 7, 3, 9, 9, 7]), 4);
assert.equal(longestTwoKinds([5, 5, 5]), 3);
assert.equal(longestTwoKinds([]), 0);
assert.equal(longestTwoKinds([1, 2, 3, 4]), 2);
assert.equal(longestTwoKinds([1, 2, 1, 3, 3, 3, 1]), 5);
assert.equal(longestTwoKinds([4]), 1);
console.log("060 ok");

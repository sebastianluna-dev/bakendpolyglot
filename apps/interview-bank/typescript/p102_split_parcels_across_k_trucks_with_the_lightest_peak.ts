// 102 · Split Parcels Across k Trucks With the Lightest Peak
import assert from "node:assert/strict";

export function lightestPeak(weights: number[], k: number): number {
  let lo = Math.max(...weights);
  let hi = weights.reduce((sum, w) => sum + w, 0);
  while (lo < hi) {
    const mid = lo + Math.floor((hi - lo) / 2);
    if (trucksNeeded(weights, mid) <= k) hi = mid;
    else lo = mid + 1;
  }
  return lo;
}

function trucksNeeded(weights: number[], limit: number): number {
  let trucks = 1;
  let load = 0;
  for (const w of weights) {
    if (load + w > limit) {
      trucks++;
      load = 0;
    }
    load += w;
  }
  return trucks;
}

assert.equal(lightestPeak([4, 9, 3, 6, 8, 2], 3), 13);
assert.equal(lightestPeak([3, 1, 12, 2], 3), 12);
assert.equal(lightestPeak([4, 9, 3], 1), 16);
assert.equal(lightestPeak([2, 8, 5], 3), 8);
assert.equal(lightestPeak([7], 1), 7);
assert.equal(lightestPeak([1, 1, 1, 1, 1, 1], 4), 2);
console.log("102 ok");

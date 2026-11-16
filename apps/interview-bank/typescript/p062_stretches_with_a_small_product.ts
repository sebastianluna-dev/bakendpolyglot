// 062 · Stretches With a Small Product
import assert from "node:assert/strict";

export function countSmallProducts(factors: number[], limit: number): number {
  if (limit <= 1) return 0; // every product is at least 1
  let left = 0;
  let product = 1; // product of factors[left..right]
  let count = 0;
  for (let right = 0; right < factors.length; right++) {
    product *= factors[right];
    while (product >= limit) {
      product /= factors[left];
      left++;
    }
    count += right - left + 1; // every stretch that ends at right and starts at left or later
  }
  return count;
}

assert.equal(countSmallProducts([4, 2, 6, 1, 3], 15), 10);
assert.equal(countSmallProducts([1, 1, 1], 1), 0);
assert.equal(countSmallProducts([1, 1, 1], 2), 6);
assert.equal(countSmallProducts([], 10), 0);
assert.equal(countSmallProducts([7], 0), 0);
assert.equal(countSmallProducts([10, 5, 2], 100), 5);
assert.equal(countSmallProducts([1000, 1000], 1000000), 2);
console.log("062 ok");

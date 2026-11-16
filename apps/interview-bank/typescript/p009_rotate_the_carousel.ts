// 009 · Rotate the Carousel
import assert from "node:assert/strict";

export function rotateCarousel(slides: number[], k: number): void {
  const n = slides.length;
  if (n === 0) return;
  k %= n;
  reverse(slides, 0, n - 1);
  reverse(slides, 0, k - 1);
  reverse(slides, k, n - 1);
}

function reverse(a: number[], lo: number, hi: number): void {
  for (; lo < hi; lo++, hi--) [a[lo], a[hi]] = [a[hi], a[lo]];
}

function rotated(slides: number[], k: number): number[] {
  rotateCarousel(slides, k);
  return slides;
}

assert.deepEqual(rotated([1, 2, 3, 4, 5], 2), [4, 5, 1, 2, 3]);
assert.deepEqual(rotated([10, 20], 5), [20, 10]);
assert.deepEqual(rotated([1, 2, 3], 0), [1, 2, 3]);
assert.deepEqual(rotated([1, 2, 3], 3), [1, 2, 3]);
assert.deepEqual(rotated([7], 4), [7]);
assert.deepEqual(rotated([], 3), []);
console.log("009 ok");
